package com.opaltrace.platform.analytics.domain.model.events;

import com.opaltrace.platform.analytics.domain.model.valueobjects.ComplianceStatus;
import java.time.LocalDateTime;

public record EsgComplianceUpdatedEvent(
        Long reportId,
        ComplianceStatus complianceStatus,
        LocalDateTime timestamp
) {}
