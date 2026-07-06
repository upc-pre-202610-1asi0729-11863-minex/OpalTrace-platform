package com.opaltrace.platform.consumerexperience.domain.model.commands;

import jakarta.validation.constraints.NotBlank;

public record VerifyProductAuthenticityCommand(
        @NotBlank String certificateId,
        String verifierIp,
        Long consumerId
) {
    public VerifyProductAuthenticityCommand(String certificateId, String verifierIp) {
        this(certificateId, verifierIp, null);
    }
}
