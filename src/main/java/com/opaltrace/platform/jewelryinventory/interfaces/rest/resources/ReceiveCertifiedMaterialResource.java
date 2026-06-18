package com.opaltrace.platform.jewelryinventory.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReceiveCertifiedMaterialResource(
        @NotNull Long batchPk,
        @NotBlank String batchId,
        Long jewelryId,
        @NotBlank String name,
        @Positive double weightGrams
) {
}
