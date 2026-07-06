package com.opaltrace.platform.subscriptions.application.internal.queryservices;

import com.opaltrace.platform.subscriptions.application.queryservices.SubscriptionQueryService;
import com.opaltrace.platform.subscriptions.domain.model.aggregates.Subscription;
import com.opaltrace.platform.subscriptions.domain.model.entities.BillingRecord;
import com.opaltrace.platform.subscriptions.domain.model.queries.*;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.SubscriptionStatus;
import com.opaltrace.platform.subscriptions.domain.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionQueryServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Optional<Subscription> handle(GetSubscriptionByUserIdQuery query) {
        return subscriptionRepository.findByUserId(query.userId());
    }

    @Override
    public Optional<Subscription> handle(GetActiveSubscriptionByUserIdQuery query) {
        return subscriptionRepository.findByUserId(query.userId())
                .filter(s -> s.getStatus() == SubscriptionStatus.ACTIVE);
    }

    @Override
    public List<BillingRecord> handle(GetBillingHistoryByUserIdQuery query) {
        return subscriptionRepository.findByUserId(query.userId())
                .map(s -> s.getBillingRecords().stream()
                        .sorted(Comparator.comparing(BillingRecord::getTransactionDate).reversed())
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    @Override
    public List<Subscription> handle(GetAllSubscriptionsQuery query) {
        return subscriptionRepository.findAll();
    }
}
