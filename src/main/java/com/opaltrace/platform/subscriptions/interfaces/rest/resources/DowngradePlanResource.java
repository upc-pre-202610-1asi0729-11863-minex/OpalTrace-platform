package com.opaltrace.platform.subscriptions.interfaces.rest.resources;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import jakarta.validation.constraints.NotNull;

public record DowngradePlanResource(
        Long userId,
        @NotNull PlanTier targetTier
) {}
