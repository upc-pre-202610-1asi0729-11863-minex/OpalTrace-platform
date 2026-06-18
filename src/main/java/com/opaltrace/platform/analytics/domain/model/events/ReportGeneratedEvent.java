package com.opaltrace.platform.analytics.domain.model.events;

import com.opaltrace.platform.analytics.domain.model.valueobjects.ReportType;
import java.time.LocalDateTime;

public record ReportGeneratedEvent(
        Long reportId,
        ReportType reportType,
        Long requestedByUserId,
        LocalDateTime timestamp
) {}
