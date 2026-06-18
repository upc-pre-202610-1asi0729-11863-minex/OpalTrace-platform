package com.opaltrace.platform.subscriptions.domain.model.events;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PlanDowngradeScheduledEvent(
        Long subscriptionId,
        Long userId,
        PlanTier previousTier,
        PlanTier targetTier,
        LocalDate effectiveDate,
        LocalDateTime timestamp
) {}
