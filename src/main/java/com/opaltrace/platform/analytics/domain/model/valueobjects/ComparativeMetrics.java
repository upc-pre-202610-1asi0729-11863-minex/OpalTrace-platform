package com.opaltrace.platform.analytics.domain.model.valueobjects;

public record ComparativeMetrics(
        String period1Label,
        String period2Label,
        long period1TotalBatches,
        long period2TotalBatches,
        double period1AvgTransitHours,
        double period2AvgTransitHours,
        long period1AnomaliesCount,
        long period2AnomaliesCount,
        long period1CertifiedCount,
        long period2CertifiedCount
) {}
