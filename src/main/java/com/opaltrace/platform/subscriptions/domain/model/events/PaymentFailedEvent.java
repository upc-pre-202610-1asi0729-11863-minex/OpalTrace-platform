package com.opaltrace.platform.subscriptions.domain.model.events;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import java.time.LocalDateTime;

public record PaymentFailedEvent(
        Long subscriptionId,
        Long userId,
        PlanTier planTier,
        String reason,
        LocalDateTime timestamp
) {}
