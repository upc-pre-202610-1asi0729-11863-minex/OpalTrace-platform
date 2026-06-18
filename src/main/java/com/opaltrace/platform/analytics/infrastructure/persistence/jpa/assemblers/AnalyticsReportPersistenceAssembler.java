package com.opaltrace.platform.analytics.infrastructure.persistence.jpa.assemblers;

import com.opaltrace.platform.analytics.domain.model.aggregates.AnalyticsReport;
import com.opaltrace.platform.analytics.infrastructure.persistence.jpa.entities.AnalyticsReportPersistenceEntity;

public final class AnalyticsReportPersistenceAssembler {

    private AnalyticsReportPersistenceAssembler() {}

    public static AnalyticsReport toDomain(AnalyticsReportPersistenceEntity entity) {
        var report = new AnalyticsReport();
        report.setId(entity.getId());
        report.reconstitute(
                entity.getReportType(),
                entity.getRequestedByUserId(),
                entity.getPeriodStart(),
                entity.getPeriodEnd(),
                entity.getGeneratedAt(),
                entity.getReportData(),
                entity.getComplianceStatus()
        );
        return report;
    }

    public static AnalyticsReportPersistenceEntity toPersistence(AnalyticsReport report) {
        var entity = new AnalyticsReportPersistenceEntity();
        if (report.getId() != null) {
            entity.setId(report.getId());
        }
        entity.setReportType(report.getReportType());
        entity.setRequestedByUserId(report.getRequestedByUserId());
        entity.setPeriodStart(report.getPeriodStart());
        entity.setPeriodEnd(report.getPeriodEnd());
        entity.setGeneratedAt(report.getGeneratedAt());
        entity.setReportData(report.getReportData());
        entity.setComplianceStatus(report.getComplianceStatus());
        return entity;
    }
}
