package com.opaltrace.platform.mineralextraction.domain.model.commands;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.MineralType;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record SyncOfflineBatchCommand(
        @NotBlank String offlineBatchId,
        MineralType mineralType,
        double weightKg,
        double latitude,
        double longitude,
        Long supervisorId,
        Long miningCompanyId,
        LocalDateTime originalTimestamp
) {}
