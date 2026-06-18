package com.opaltrace.platform.mineralextraction.interfaces.rest.resources;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.MineralType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RegisterMineralBatchResource(
        @NotNull MineralType mineralType,
        @Positive double weightKg,
        @NotNull Double latitude,
        @NotNull Double longitude,
        Long supervisorId,
        Long miningCompanyId
) {}
