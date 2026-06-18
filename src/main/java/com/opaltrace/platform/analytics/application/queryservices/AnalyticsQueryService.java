package com.opaltrace.platform.analytics.application.queryservices;

import com.opaltrace.platform.analytics.domain.model.aggregates.AnalyticsReport;
import com.opaltrace.platform.analytics.domain.model.queries.*;
import com.opaltrace.platform.analytics.domain.model.valueobjects.ComparativeMetrics;
import com.opaltrace.platform.analytics.domain.model.valueobjects.DashboardMetrics;
import com.opaltrace.platform.analytics.domain.model.valueobjects.ShrinkageIndicator;

import java.util.List;
import java.util.Optional;

public interface AnalyticsQueryService {
    DashboardMetrics handle(GetDashboardMetricsQuery query);
    List<ShrinkageIndicator> handle(GetShrinkageIndicatorsQuery query);
    Optional<AnalyticsReport> handle(GetEsgReportQuery query);
    ComparativeMetrics handle(GetComparativeAnalysisQuery query);
    List<AnalyticsReport> handle(GetAllReportsQuery query);
}
