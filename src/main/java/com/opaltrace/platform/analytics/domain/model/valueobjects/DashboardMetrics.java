package com.opaltrace.platform.analytics.domain.model.valueobjects;

import java.time.LocalDateTime;

public record DashboardMetrics(
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
