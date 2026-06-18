package com.opaltrace.platform.mineralextraction.interfaces.rest.resources;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.MineralType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SyncOfflineBatchResource(
        @NotBlank String offlineBatchId,
        @NotNull MineralType mineralType,
        double weightKg,
        double latitude,
        double longitude,
        Long supervisorId,
        Long miningCompanyId,
        LocalDateTime originalTimestamp
) {}
