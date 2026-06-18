package com.opaltrace.platform.jewelryinventory.domain.model.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReceiveCertifiedMaterialCommand(
        @NotNull Long batchPk,
        @NotBlank String batchId,
        Long jewelryId,
        @NotBlank String name,
        @Positive double weightGrams
) {
}
