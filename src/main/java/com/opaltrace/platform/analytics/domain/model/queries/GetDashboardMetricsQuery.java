package com.opaltrace.platform.analytics.domain.model.queries;

import java.time.LocalDate;

public record GetDashboardMetricsQuery(
        Long requestedByUserId,
        LocalDate fromDate,
        LocalDate toDate,
        String mineralTypeFilter
) {}
