package com.opaltrace.platform.refineryprocessing.domain.model.events;

import java.time.LocalDateTime;

public record ProcessingCompletedEvent(
        String batchId,
        Long batchPk,
        Long supervisorId,
        LocalDateTime timestamp,
        double shrinkagePercent
) {}
