package com.opaltrace.platform.consumerexperience.domain.model.events;

import java.time.LocalDateTime;

public record SuspiciousVerificationAttemptEvent(String certificateId, String suspiciousIp, LocalDateTime timestamp) {
}
