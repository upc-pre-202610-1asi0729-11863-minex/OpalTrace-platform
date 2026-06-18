package com.opaltrace.platform.consumerexperience.interfaces.rest.resources;

import com.opaltrace.platform.consumerexperience.domain.model.valueobjects.VerificationResult;

import java.time.LocalDateTime;

public record ProductVerificationResultResource(
        String certificateId,
        VerificationResult result,
        String failureReason,
        Long productId,
        String batchId,
        LocalDateTime verifiedAt,
        String message
) {
}
