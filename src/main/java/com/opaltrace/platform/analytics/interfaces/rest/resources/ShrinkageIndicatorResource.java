package com.opaltrace.platform.analytics.interfaces.rest.resources;

public record ShrinkageIndicatorResource(
        String batchId,
        Long batchPk,
        double originalWeightKg,
        double finalWeightKg,
        double shrinkagePercent,
        boolean exceedsThreshold
) {}
