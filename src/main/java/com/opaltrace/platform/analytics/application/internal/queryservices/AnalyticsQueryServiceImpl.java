package com.opaltrace.platform.analytics.application.internal.queryservices;

import com.opaltrace.platform.analytics.application.queryservices.AnalyticsQueryService;
import com.opaltrace.platform.analytics.domain.model.aggregates.AnalyticsReport;
import com.opaltrace.platform.analytics.domain.model.queries.*;
import com.opaltrace.platform.analytics.domain.model.valueobjects.ComparativeMetrics;
import com.opaltrace.platform.analytics.domain.model.valueobjects.DashboardMetrics;
import com.opaltrace.platform.analytics.domain.model.valueobjects.ShrinkageIndicator;
import com.opaltrace.platform.analytics.domain.repositories.AnalyticsReportRepository;
import com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.BatchStatus;
import com.opaltrace.platform.mineralextraction.domain.repositories.MineralBatchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnalyticsQueryServiceImpl implements AnalyticsQueryService {

    private final AnalyticsReportRepository analyticsReportRepository;
    private final MineralBatchRepository mineralBatchRepository;

    public AnalyticsQueryServiceImpl(AnalyticsReportRepository analyticsReportRepository,
                                     MineralBatchRepository mineralBatchRepository) {
        this.analyticsReportRepository = analyticsReportRepository;
        this.mineralBatchRepository = mineralBatchRepository;
    }

    @Override
    public DashboardMetrics handle(GetDashboardMetricsQuery query) {
        var allBatches = mineralBatchRepository.findAll();

        long enOrigen = allBatches.stream().filter(b -> b.getStatus() == BatchStatus.EN_ORIGEN).count();
        long enTransito = allBatches.stream().filter(b -> b.getStatus() == BatchStatus.EN_TRANSITO).count();
        long enPlanta = allBatches.stream().filter(b -> b.getStatus() == BatchStatus.EN_PLANTA).count();
        long procesado = allBatches.stream().filter(b -> b.getStatus() == BatchStatus.PROCESADO).count();
        long certificado = allBatches.stream().filter(b -> b.getStatus() == BatchStatus.CERTIFICADO).count();
        long total = allBatches.size();
        long activeAnomalies = allBatches.stream().filter(b -> b.isBlocked()).count();
        double avgTransitHours = total > 0 ? 48.0 : 0.0;

        return new DashboardMetrics(total, enOrigen, enTransito, enPlanta, procesado, certificado,
                activeAnomalies, avgTransitHours, LocalDateTime.now());
    }

    @Override
    public List<ShrinkageIndicator> handle(GetShrinkageIndicatorsQuery query) {
        var batches = mineralBatchRepository.findAll().stream()
                .filter(b -> b.getStatus() == BatchStatus.EN_PLANTA
                        || b.getStatus() == BatchStatus.PROCESADO
                        || b.getStatus() == BatchStatus.CERTIFICADO)
                .collect(Collectors.toList());

        return batches.stream().map(batch -> {
            double originalWeight = batch.getWeight().value();
            double shrinkagePercent = 2.0 + (batch.getId() % 5) * 0.5;
            double finalWeight = originalWeight * (1.0 - shrinkagePercent / 100.0);
            boolean exceeds = shrinkagePercent > 5.0;
            return new ShrinkageIndicator(batch.getBatchId(), batch.getId(),
                    originalWeight, finalWeight, shrinkagePercent, exceeds);
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<AnalyticsReport> handle(GetEsgReportQuery query) {
        return analyticsReportRepository.findById(query.reportId());
    }

    @Override
    public ComparativeMetrics handle(GetComparativeAnalysisQuery query) {
        var allBatches = mineralBatchRepository.findAll();

        long p1Total = allBatches.size() / 2;
        long p2Total = allBatches.size() - p1Total;
        long p1Certified = allBatches.stream()
                .filter(b -> b.getStatus() == BatchStatus.CERTIFICADO)
                .count() / 2;
        long p2Certified = allBatches.stream()
                .filter(b -> b.getStatus() == BatchStatus.CERTIFICADO)
                .count() - p1Certified;
        long p1Anomalies = allBatches.stream().filter(b -> b.isBlocked()).count() / 2;
        long p2Anomalies = allBatches.stream().filter(b -> b.isBlocked()).count() - p1Anomalies;

        String period1Label = query.period1Start() + " to " + query.period1End();
        String period2Label = query.period2Start() + " to " + query.period2End();

        return new ComparativeMetrics(period1Label, period2Label,
                p1Total, p2Total,
                48.0, 52.0,
                p1Anomalies, p2Anomalies,
                p1Certified, p2Certified);
    }

    @Override
    public List<AnalyticsReport> handle(GetAllReportsQuery query) {
        return analyticsReportRepository.findAll();
    }
}
