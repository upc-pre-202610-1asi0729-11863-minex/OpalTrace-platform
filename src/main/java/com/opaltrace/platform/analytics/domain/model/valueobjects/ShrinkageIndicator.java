package com.opaltrace.platform.analytics.domain.model.valueobjects;

public record ShrinkageIndicator(
        String batchId,
        Long batchPk,
        double originalWeightKg,
        double finalWeightKg,
        double shrinkagePercent,
        boolean exceedsThreshold
) {}
