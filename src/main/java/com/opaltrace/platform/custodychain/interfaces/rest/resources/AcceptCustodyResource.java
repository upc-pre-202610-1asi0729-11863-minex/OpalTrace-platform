package com.opaltrace.platform.custodychain.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AcceptCustodyResource(
        @NotBlank String batchId,
        Long custodyHolderUserId,
        @NotNull Double latitude,
        @NotNull Double longitude
) {}
