package com.opaltrace.platform.refineryprocessing.domain.model.events;

import java.time.LocalDateTime;

public record BatchReceivedAtRefineryEvent(
        String batchId,
        Long batchPk,
        Long refineryId,
        Long supervisorId,
        double declaredWeightKg,
        double originalWeightKg,
        double weightDiscrepancyPercent,
        LocalDateTime timestamp
) {}
