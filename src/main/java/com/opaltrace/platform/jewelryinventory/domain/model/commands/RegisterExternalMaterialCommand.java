package com.opaltrace.platform.jewelryinventory.domain.model.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record RegisterExternalMaterialCommand(
        Long jewelryId,
        @NotBlank String name,
        @NotBlank String externalSupplierName,
        @Positive double weightGrams,
        String description
) {
}
