package com.opaltrace.platform.subscriptions.domain.model.aggregates;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.opaltrace.platform.subscriptions.domain.model.commands.ActivateSubscriptionCommand;
import com.opaltrace.platform.subscriptions.domain.model.entities.BillingRecord;
import com.opaltrace.platform.subscriptions.domain.model.events.*;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.BillingCycle;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.SubscriptionStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Subscription extends AbstractDomainAggregateRoot<Subscription> {

    private Long id;
    private Long userId;
    private PlanTier planTier;
    private SubscriptionStatus status;
    private BillingCycle billingCycle;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextBillingDate;
    private boolean pendingDowngrade;
    private PlanTier targetDowngradeTier;
    private LocalDate downgradeEffectiveDate;
    private LocalDate cancelEffectiveDate;
    private List<BillingRecord> billingRecords;

    public Subscription() {
        this.billingRecords = new ArrayList<>();
    }

    public Subscription(ActivateSubscriptionCommand cmd) {
        this.billingRecords = new ArrayList<>();
        this.userId = cmd.userId();
        this.planTier = cmd.planTier();
        this.status = SubscriptionStatus.ACTIVE;
        this.billingCycle = cmd.billingCycle() != null ? cmd.billingCycle() : BillingCycle.MONTHLY;
        this.startDate = LocalDate.now();
        this.nextBillingDate = LocalDate.now().plusMonths(1);
        this.endDate = null;
        this.pendingDowngrade = false;
        registerDomainEvent(new SubscriptionActivatedEvent(null, cmd.userId(), cmd.planTier(), LocalDateTime.now()));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void upgradePlan(PlanTier newTier) {
        this.planTier = newTier;
        registerDomainEvent(new PlanUpgradedEvent(this.id, this.userId, this.planTier, newTier, 0.0, LocalDateTime.now()));
    }

    public void scheduleDowngrade(PlanTier targetTier) {
        this.pendingDowngrade = true;
        this.targetDowngradeTier = targetTier;
        this.downgradeEffectiveDate = this.nextBillingDate;
        registerDomainEvent(new PlanDowngradeScheduledEvent(this.id, this.userId, this.planTier,
                targetTier, this.downgradeEffectiveDate, LocalDateTime.now()));
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
        this.cancelEffectiveDate = this.nextBillingDate;
        registerDomainEvent(new SubscriptionCancelledEvent(this.id, this.userId,
                this.cancelEffectiveDate, null, LocalDateTime.now()));
    }

    public void addBillingRecord(BillingRecord record) {
        this.billingRecords.add(record);
    }

    public void reconstitute(Long userId, PlanTier planTier, SubscriptionStatus status,
                             BillingCycle billingCycle, LocalDate startDate, LocalDate endDate,
                             LocalDate nextBillingDate, boolean pendingDowngrade,
                             PlanTier targetDowngradeTier, LocalDate downgradeEffectiveDate,
                             LocalDate cancelEffectiveDate, List<BillingRecord> billingRecords) {
        this.userId = userId;
        this.planTier = planTier;
        this.status = status;
        this.billingCycle = billingCycle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nextBillingDate = nextBillingDate;
        this.pendingDowngrade = pendingDowngrade;
        this.targetDowngradeTier = targetDowngradeTier;
        this.downgradeEffectiveDate = downgradeEffectiveDate;
        this.cancelEffectiveDate = cancelEffectiveDate;
        this.billingRecords = billingRecords != null ? billingRecords : new ArrayList<>();
    }
}
