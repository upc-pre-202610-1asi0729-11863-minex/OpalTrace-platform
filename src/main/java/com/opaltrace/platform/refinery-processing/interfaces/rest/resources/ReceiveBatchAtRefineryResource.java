package com.opaltrace.platform.refineryprocessing.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ReceiveBatchAtRefineryResource(
        @NotBlank String batchId,
        Long refineryId,
        Long supervisorId,
        @Positive double declaredWeightKg
) {}
