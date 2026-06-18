package com.opaltrace.platform.analytics.interfaces.rest.resources;

import java.time.LocalDateTime;

public record DashboardMetricsResource(
        long totalBatches,
        long batchesEnOrigen,
        long batchesEnTransito,
        long batchesEnPlanta,
        long batchesProcesado,
        long batchesCertificado,
        long activeAnomalies,
        double avgTransitTimeHours,
        LocalDateTime generatedAt
) {}
