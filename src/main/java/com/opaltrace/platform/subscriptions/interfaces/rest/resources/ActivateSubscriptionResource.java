package com.opaltrace.platform.subscriptions.interfaces.rest.resources;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.BillingCycle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ActivateSubscriptionResource(
        @NotNull Long userId,
        @NotNull PlanTier planTier,
        BillingCycle billingCycle,
        @NotBlank String paymentMethodToken,
        @Positive double amount
) {}
