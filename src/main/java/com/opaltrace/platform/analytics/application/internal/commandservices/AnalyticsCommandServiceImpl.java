package com.opaltrace.platform.analytics.application.internal.commandservices;

import com.opaltrace.platform.analytics.application.commandservices.AnalyticsCommandService;
import com.opaltrace.platform.analytics.domain.model.aggregates.AnalyticsReport;
import com.opaltrace.platform.analytics.domain.model.commands.GenerateComparativeAnalysisCommand;
import com.opaltrace.platform.analytics.domain.model.commands.GenerateEsgReportCommand;
import com.opaltrace.platform.analytics.domain.model.events.EsgComplianceUpdatedEvent;
import com.opaltrace.platform.analytics.domain.model.events.ReportGeneratedEvent;
import com.opaltrace.platform.analytics.domain.model.valueobjects.ComplianceStatus;
import com.opaltrace.platform.analytics.domain.model.valueobjects.ReportType;
import com.opaltrace.platform.analytics.domain.repositories.AnalyticsReportRepository;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.BatchStatus;
import com.opaltrace.platform.mineralextraction.domain.repositories.MineralBatchRepository;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class AnalyticsCommandServiceImpl implements AnalyticsCommandService {

    private final AnalyticsReportRepository analyticsReportRepository;
    private final MineralBatchRepository mineralBatchRepository;

    public AnalyticsCommandServiceImpl(AnalyticsReportRepository analyticsReportRepository,
                                       MineralBatchRepository mineralBatchRepository) {
        this.analyticsReportRepository = analyticsReportRepository;
        this.mineralBatchRepository = mineralBatchRepository;
    }

    @Override
    public Result<Long, ApplicationError> handle(GenerateEsgReportCommand command) {
        try {
            var allBatches = mineralBatchRepository.findAll();
            var filteredBatches = allBatches.stream()
                    .filter(b -> {
                        if (command.periodStart() == null || command.periodEnd() == null) return true;
                        return true;
                    })
                    .collect(Collectors.toList());

            long totalBatches = filteredBatches.size();
            long certifiedCount = filteredBatches.stream()
                    .filter(b -> b.getStatus() == BatchStatus.CERTIFICADO)
                    .count();
            long anomalyCount = filteredBatches.stream()
                    .filter(b -> b.hasActiveAnomalies())
                    .count();

            double certifiedPercent = totalBatches > 0 ? (certifiedCount * 100.0 / totalBatches) : 0.0;
            long totalDistanceKm = totalBatches * 150L;
            double co2Estimate = totalDistanceKm * 0.21;

            String reportData = "{\"certifiedBatchesPercent\":" + String.format("%.2f", certifiedPercent)
                    + ",\"co2EstimateKg\":" + String.format("%.2f", co2Estimate)
                    + ",\"totalBatches\":" + totalBatches
                    + ",\"certifiedCount\":" + certifiedCount
                    + ",\"anomalyCount\":" + anomalyCount
                    + ",\"periodStart\":\"" + command.periodStart() + "\""
                    + ",\"periodEnd\":\"" + command.periodEnd() + "\"}";

            ComplianceStatus complianceStatus;
            if (certifiedPercent >= 80.0) {
                complianceStatus = ComplianceStatus.COMPLIANT;
            } else if (certifiedPercent >= 50.0) {
                complianceStatus = ComplianceStatus.PARTIAL;
            } else {
                complianceStatus = ComplianceStatus.NON_COMPLIANT;
            }

            var report = new AnalyticsReport(command);
            report.setReportData(reportData);
            report.setComplianceStatus(complianceStatus);
            var savedReport = analyticsReportRepository.save(report);

            savedReport.registerDomainEvent(new ReportGeneratedEvent(
                    savedReport.getId(), ReportType.ESG, command.requestedByUserId(), LocalDateTime.now()));
            savedReport.registerDomainEvent(new EsgComplianceUpdatedEvent(
                    savedReport.getId(), complianceStatus, LocalDateTime.now()));

            return Result.success(savedReport.getId());
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("GenerateEsgReport", e.getMessage()));
        }
    }

    @Override
    public Result<Long, ApplicationError> handle(GenerateComparativeAnalysisCommand command) {
        try {
            var report = new AnalyticsReport(command);
            String reportData = "{\"period1Start\":\"" + command.period1Start() + "\""
                    + ",\"period1End\":\"" + command.period1End() + "\""
                    + ",\"period2Start\":\"" + command.period2Start() + "\""
                    + ",\"period2End\":\"" + command.period2End() + "\"}";
            report.setReportData(reportData);
            var savedReport = analyticsReportRepository.save(report);
            return Result.success(savedReport.getId());
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("GenerateComparativeAnalysis", e.getMessage()));
        }
    }
}
