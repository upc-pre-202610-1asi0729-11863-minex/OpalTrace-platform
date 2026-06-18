package com.opaltrace.platform.analytics.domain.model.events;

import java.time.LocalDateTime;

public record WeightLossAlertTriggeredEvent(
        String batchId,
        Long batchPk,
        double weightLossPercent,
        double threshold,
        LocalDateTime timestamp
) {}
