package com.opaltrace.platform.subscriptions.domain.model.commands;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.BillingCycle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActivateSubscriptionCommand(
        @NotNull Long userId,
        @NotNull PlanTier planTier,
        BillingCycle billingCycle,
        @NotBlank String paymentMethodToken,
        double amount
) {}
