package com.opaltrace.platform.analytics.domain.model.aggregates;

import com.opaltrace.platform.analytics.domain.model.commands.GenerateComparativeAnalysisCommand;
import com.opaltrace.platform.analytics.domain.model.commands.GenerateEsgReportCommand;
import com.opaltrace.platform.analytics.domain.model.events.ReportGeneratedEvent;
import com.opaltrace.platform.analytics.domain.model.valueobjects.ComplianceStatus;
import com.opaltrace.platform.analytics.domain.model.valueobjects.ReportType;
import com.opaltrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class AnalyticsReport extends AbstractDomainAggregateRoot<AnalyticsReport> {

    private Long id;
    private ReportType reportType;
    private Long requestedByUserId;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private LocalDateTime generatedAt;
    private String reportData;
    private ComplianceStatus complianceStatus;

    public AnalyticsReport() {}

    public AnalyticsReport(GenerateEsgReportCommand cmd) {
        this.reportType = ReportType.ESG;
        this.requestedByUserId = cmd.requestedByUserId();
        this.periodStart = cmd.periodStart();
        this.periodEnd = cmd.periodEnd();
        this.generatedAt = LocalDateTime.now();
        registerDomainEvent(new ReportGeneratedEvent(null, ReportType.ESG, cmd.requestedByUserId(), this.generatedAt));
    }

    public AnalyticsReport(GenerateComparativeAnalysisCommand cmd) {
        this.reportType = ReportType.COMPARATIVE;
        this.requestedByUserId = cmd.requestedByUserId();
        this.periodStart = cmd.period1Start();
        this.periodEnd = cmd.period2End();
        this.generatedAt = LocalDateTime.now();
        registerDomainEvent(new ReportGeneratedEvent(null, ReportType.COMPARATIVE, cmd.requestedByUserId(), this.generatedAt));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReportData(String reportData) {
        this.reportData = reportData;
    }

    public void setComplianceStatus(ComplianceStatus complianceStatus) {
        this.complianceStatus = complianceStatus;
    }

    public void reconstitute(ReportType reportType, Long requestedByUserId, LocalDate periodStart,
                             LocalDate periodEnd, LocalDateTime generatedAt, String reportData,
                             ComplianceStatus complianceStatus) {
        this.reportType = reportType;
        this.requestedByUserId = requestedByUserId;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.generatedAt = generatedAt;
        this.reportData = reportData;
        this.complianceStatus = complianceStatus;
    }
}
