package com.opaltrace.platform.consumerexperience.domain.model.events;

import com.opaltrace.platform.consumerexperience.domain.model.valueobjects.VerificationResult;

import java.time.LocalDateTime;

public record AuthenticityVerifiedEvent(
        String certificateId,
        VerificationResult result,
        Long productId,
        String verifierIp,
        LocalDateTime timestamp
) {
}
