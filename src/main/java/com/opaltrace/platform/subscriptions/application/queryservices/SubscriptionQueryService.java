package com.opaltrace.platform.subscriptions.application.queryservices;

import com.opaltrace.platform.subscriptions.domain.model.aggregates.Subscription;
import com.opaltrace.platform.subscriptions.domain.model.entities.BillingRecord;
import com.opaltrace.platform.subscriptions.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface SubscriptionQueryService {
    Optional<Subscription> handle(GetSubscriptionByUserIdQuery query);
    Optional<Subscription> handle(GetActiveSubscriptionByUserIdQuery query);
    List<BillingRecord> handle(GetBillingHistoryByUserIdQuery query);
    List<Subscription> handle(GetAllSubscriptionsQuery query);
}
