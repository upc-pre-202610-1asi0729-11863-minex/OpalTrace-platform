package com.opaltrace.platform.subscriptions.domain.model.commands;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import jakarta.validation.constraints.NotNull;

public record UpgradePlanCommand(
        @NotNull Long subscriptionId,
        Long userId,
        @NotNull PlanTier newTier,
        String paymentMethodToken
) {}
