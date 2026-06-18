package com.opaltrace.platform.analytics.interfaces.rest.transform;

import com.opaltrace.platform.analytics.domain.model.aggregates.AnalyticsReport;
import com.opaltrace.platform.analytics.domain.model.valueobjects.ComparativeMetrics;
import com.opaltrace.platform.analytics.domain.model.valueobjects.DashboardMetrics;
import com.opaltrace.platform.analytics.domain.model.valueobjects.ShrinkageIndicator;
import com.opaltrace.platform.analytics.interfaces.rest.resources.*;

public final class AnalyticsResourceFromEntityAssembler {

    private AnalyticsResourceFromEntityAssembler() {}

    public static AnalyticsReportResource toResource(AnalyticsReport report) {
        return new AnalyticsReportResource(
                report.getId(),
                report.getReportType(),
                report.getRequestedByUserId(),
                report.getPeriodStart(),
                report.getPeriodEnd(),
                report.getGeneratedAt(),
                report.getReportData(),
                report.getComplianceStatus()
        );
    }

    public static DashboardMetricsResource toDashboardResource(DashboardMetrics metrics) {
        return new DashboardMetricsResource(
                metrics.totalBatches(),
                metrics.batchesEnOrigen(),
                metrics.batchesEnTransito(),
                metrics.batchesEnPlanta(),
                metrics.batchesProcesado(),
                metrics.batchesCertificado(),
                metrics.activeAnomalies(),
                metrics.avgTransitTimeHours(),
                metrics.generatedAt()
        );
    }

    public static ShrinkageIndicatorResource toShrinkageResource(ShrinkageIndicator indicator) {
        return new ShrinkageIndicatorResource(
                indicator.batchId(),
                indicator.batchPk(),
                indicator.originalWeightKg(),
                indicator.finalWeightKg(),
                indicator.shrinkagePercent(),
                indicator.exceedsThreshold()
        );
    }

    public static ComparativeMetricsResource toComparativeResource(ComparativeMetrics metrics) {
        return new ComparativeMetricsResource(
                metrics.period1Label(),
                metrics.period2Label(),
                metrics.period1TotalBatches(),
                metrics.period2TotalBatches(),
                metrics.period1AvgTransitHours(),
                metrics.period2AvgTransitHours(),
                metrics.period1AnomaliesCount(),
                metrics.period2AnomaliesCount(),
                metrics.period1CertifiedCount(),
                metrics.period2CertifiedCount()
        );
    }
}
