package com.opaltrace.platform.subscriptions.domain.model.events;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import java.time.LocalDateTime;

public record PlanDowngradedEvent(
        Long subscriptionId,
        Long userId,
        PlanTier previousTier,
        PlanTier newTier,
        LocalDateTime timestamp
) {}
