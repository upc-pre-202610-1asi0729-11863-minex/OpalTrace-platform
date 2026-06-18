package com.opaltrace.platform.analytics.domain.exceptions;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(Long reportId) {
        super("Report not found with id: " + reportId);
    }
}
