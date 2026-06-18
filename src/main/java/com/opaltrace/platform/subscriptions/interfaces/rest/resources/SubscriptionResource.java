package com.opaltrace.platform.subscriptions.interfaces.rest.resources;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.BillingCycle;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.SubscriptionStatus;

import java.time.LocalDate;

public record SubscriptionResource(
        Long id,
        Long userId,
        PlanTier planTier,
        SubscriptionStatus status,
        BillingCycle billingCycle,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate nextBillingDate,
        boolean pendingDowngrade,
        PlanTier targetDowngradeTier,
        LocalDate downgradeEffectiveDate,
        LocalDate cancelEffectiveDate
) {}
