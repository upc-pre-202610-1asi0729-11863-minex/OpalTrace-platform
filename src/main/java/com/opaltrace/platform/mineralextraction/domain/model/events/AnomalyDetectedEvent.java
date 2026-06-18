package com.opaltrace.platform.mineralextraction.domain.model.events;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.AnomalyCategory;

import java.time.LocalDateTime;

public record AnomalyDetectedEvent(
        String batchId,
        AnomalyCategory category,
        String description,
        Long reportedByUserId,
        LocalDateTime timestamp
) {}
