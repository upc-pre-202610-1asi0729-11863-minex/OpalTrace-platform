package com.opaltrace.platform.analytics.interfaces.rest.resources;

public record ComparativeMetricsResource(
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
