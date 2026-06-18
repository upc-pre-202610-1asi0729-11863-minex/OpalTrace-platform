package com.opaltrace.platform.subscriptions.domain.model.events;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SubscriptionCancelledEvent(
        Long subscriptionId,
        Long userId,
        LocalDate effectiveDate,
        String reason,
        LocalDateTime timestamp
) {}
