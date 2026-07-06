package com.opaltrace.platform.subscriptions.application.internal.commandservices;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.iam.domain.repositories.UserRepository;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.BatchStatus;
import com.opaltrace.platform.mineralextraction.domain.repositories.MineralBatchRepository;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import com.opaltrace.platform.subscriptions.application.PaymentGatewayService;
import com.opaltrace.platform.subscriptions.application.commandservices.SubscriptionCommandService;
import com.opaltrace.platform.subscriptions.domain.model.aggregates.Subscription;
import com.opaltrace.platform.subscriptions.domain.model.commands.*;
import com.opaltrace.platform.subscriptions.domain.model.entities.BillingRecord;
import com.opaltrace.platform.subscriptions.domain.model.events.PlanUpgradedEvent;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.PaymentStatus;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.SubscriptionStatus;
import com.opaltrace.platform.subscriptions.domain.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    private final SubscriptionRepository subscriptionRepository;
    private final MineralBatchRepository mineralBatchRepository;
    private final UserRepository userRepository;
    private final PaymentGatewayService paymentGatewayService;

    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository,
                                          MineralBatchRepository mineralBatchRepository,
                                          UserRepository userRepository,
                                          PaymentGatewayService paymentGatewayService) {
        this.subscriptionRepository = subscriptionRepository;
        this.mineralBatchRepository = mineralBatchRepository;
        this.userRepository = userRepository;
        this.paymentGatewayService = paymentGatewayService;
    }

    @Override
    public Result<Long, ApplicationError> handle(ActivateSubscriptionCommand command) {
        try {
            if (subscriptionRepository.existsByUserId(command.userId())) {
                var existing = subscriptionRepository.findByUserId(command.userId());
                if (existing.isPresent() && existing.get().getStatus() == SubscriptionStatus.ACTIVE) {
                    return Result.failure(ApplicationError.conflict("Subscription",
                            "User already has an active subscription"));
                }
            }

            var paymentResult = paymentGatewayService.charge(
                    command.paymentMethodToken(), command.amount(), command.planTier());
            if (!paymentResult.success()) {
                return Result.failure(ApplicationError.paymentDeclined(paymentResult.declineReason()));
            }

            var subscription = new Subscription(command);
            var saved = subscriptionRepository.save(subscription);

            int year = LocalDate.now().getYear();
            long seq = subscriptionRepository.findAll().stream()
                    .flatMap(s -> s.getBillingRecords().stream()).count() + 1;
            String invoiceNumber = "FACT-%d-%04d".formatted(year, seq);

            var billingRecord = new BillingRecord(saved.getId(), invoiceNumber, command.amount(),
                    command.planTier(), PaymentStatus.COMPLETED, command.paymentMethodToken());
            saved.addBillingRecord(billingRecord);
            saved = subscriptionRepository.save(saved);

            userRepository.findById(command.userId()).ifPresent(user -> {
                user.upgradePlan(command.planTier());
                userRepository.save(user);
            });

            return Result.success(saved.getId());
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("ActivateSubscription", e.getMessage()));
        }
    }

    @Override
    public Result<Long, ApplicationError> handle(UpgradePlanCommand command) {
        try {
            var subscriptionOpt = subscriptionRepository.findById(command.subscriptionId());
            if (subscriptionOpt.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Subscription", String.valueOf(command.subscriptionId())));
            }
            var subscription = subscriptionOpt.get();
            if (subscription.getStatus() == SubscriptionStatus.SUSPENDED) {
                return Result.failure(ApplicationError.conflict("Subscription",
                        "Pending payments must be resolved before upgrading"));
            }

            PlanTier previousTier = subscription.getPlanTier();
            double previousPrice = tierPrice(previousTier);
            double newPrice = tierPrice(command.newTier());
            long daysLeft = LocalDate.now().until(subscription.getNextBillingDate()).getDays();
            double proratedAmount = (daysLeft / 30.0) * (newPrice - previousPrice);
            if (proratedAmount < 0) proratedAmount = 0;

            String token = command.paymentMethodToken() != null ? command.paymentMethodToken() : "tok_default";
            var paymentResult = paymentGatewayService.charge(token, proratedAmount, command.newTier());
            if (!paymentResult.success()) {
                return Result.failure(ApplicationError.paymentDeclined(paymentResult.declineReason()));
            }

            subscription.upgradePlan(command.newTier());

            int year = LocalDate.now().getYear();
            long seq = subscriptionRepository.findAll().stream()
                    .flatMap(s -> s.getBillingRecords().stream()).count() + 1;
            String invoiceNumber = "FACT-%d-%04d".formatted(year, seq);
            var billingRecord = new BillingRecord(subscription.getId(), invoiceNumber, proratedAmount,
                    command.newTier(), PaymentStatus.COMPLETED,
                    command.paymentMethodToken() != null ? command.paymentMethodToken() : "****0000");
            subscription.addBillingRecord(billingRecord);

            userRepository.findById(command.userId()).ifPresent(user -> {
                user.upgradePlan(command.newTier());
                userRepository.save(user);
            });

            subscription.registerDomainEvent(new PlanUpgradedEvent(subscription.getId(), command.userId(),
                    previousTier, command.newTier(), proratedAmount, LocalDateTime.now()));

            var saved = subscriptionRepository.save(subscription);
            return Result.success(saved.getId());
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("UpgradePlan", e.getMessage()));
        }
    }

    @Override
    public Result<Long, ApplicationError> handle(DowngradePlanCommand command) {
        try {
            var subscriptionOpt = subscriptionRepository.findById(command.subscriptionId());
            if (subscriptionOpt.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Subscription", String.valueOf(command.subscriptionId())));
            }
            var subscription = subscriptionOpt.get();

            if (command.targetTier() == PlanTier.GOLD || command.targetTier() == PlanTier.SILVER) {
                long enPlantaCount = mineralBatchRepository.findByStatus(BatchStatus.EN_PLANTA).size();
                if (enPlantaCount > 0) {
                    return Result.failure(ApplicationError.businessRuleViolation("IncompatibleOperations",
                            "Cannot downgrade: " + enPlantaCount + " batches are currently EN_PLANTA"));
                }
            }

            subscription.scheduleDowngrade(command.targetTier());
            var saved = subscriptionRepository.save(subscription);
            return Result.success(saved.getId());
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("DowngradePlan", e.getMessage()));
        }
    }

    @Override
    public Result<Long, ApplicationError> handle(CancelSubscriptionCommand command) {
        try {
            var subscriptionOpt = subscriptionRepository.findById(command.subscriptionId());
            if (subscriptionOpt.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Subscription", String.valueOf(command.subscriptionId())));
            }
            var subscription = subscriptionOpt.get();
            if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
                return Result.failure(ApplicationError.conflict("Subscription",
                        "Subscription is already cancelled"));
            }
            subscription.cancel();
            var saved = subscriptionRepository.save(subscription);
            return Result.success(saved.getId());
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("CancelSubscription", e.getMessage()));
        }
    }

    private double tierPrice(PlanTier tier) {
        return switch (tier) {
            case SILVER -> 9.99;
            case GOLD -> 49.99;
            case PLATINUM -> 99.99;
        };
    }
}
