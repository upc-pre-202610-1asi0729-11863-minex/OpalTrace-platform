package com.opaltrace.platform.jewelryinventory.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record RegisterExternalMaterialResource(
        Long jewelryId,
        @NotBlank String name,
        @NotBlank String externalSupplierName,
        @Positive double weightGrams,
        String description
) {
}
