package com.opaltrace.platform.mineralextraction.domain.model.events;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.MineralType;

import java.time.LocalDateTime;

public record MineralExtractedEvent(
        String batchId,
        MineralType mineralType,
        double weightKg,
        double latitude,
        double longitude,
        Long supervisorId,
        Long miningCompanyId,
        LocalDateTime timestamp
) {}
