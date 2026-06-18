package com.opaltrace.platform.consumerexperience.domain.model.commands;

import jakarta.validation.constraints.NotBlank;

public record RecordTraceabilityViewCommand(
        @NotBlank String certificateId,
        String viewerIp
) {
}
