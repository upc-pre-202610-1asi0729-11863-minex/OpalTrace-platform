package com.opaltrace.platform.analytics.interfaces.rest.resources;

import com.opaltrace.platform.analytics.domain.model.valueobjects.ComplianceStatus;
import com.opaltrace.platform.analytics.domain.model.valueobjects.ReportType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AnalyticsReportResource(
        Long id,
        ReportType reportType,
        Long requestedByUserId,
        LocalDate periodStart,
        LocalDate periodEnd,
        LocalDateTime generatedAt,
        String reportData,
        ComplianceStatus complianceStatus
) {}
