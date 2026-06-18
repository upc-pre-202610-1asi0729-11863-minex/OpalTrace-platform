package com.opaltrace.platform.analytics.domain.repositories;

import com.opaltrace.platform.analytics.domain.model.aggregates.AnalyticsReport;

import java.util.List;
import java.util.Optional;

public interface AnalyticsReportRepository {
    AnalyticsReport save(AnalyticsReport report);
    Optional<AnalyticsReport> findById(Long id);
    List<AnalyticsReport> findAll();
}
