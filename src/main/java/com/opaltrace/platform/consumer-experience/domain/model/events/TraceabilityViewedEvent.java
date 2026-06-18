package com.opaltrace.platform.consumerexperience.domain.model.events;

import java.time.LocalDateTime;

public record TraceabilityViewedEvent(String certificateId, String viewerIp, LocalDateTime timestamp) {
}
