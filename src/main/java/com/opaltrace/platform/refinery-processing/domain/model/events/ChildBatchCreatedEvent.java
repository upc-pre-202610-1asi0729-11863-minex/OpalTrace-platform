package com.opaltrace.platform.refineryprocessing.domain.model.events;

import java.time.LocalDateTime;

public record ChildBatchCreatedEvent(
        String parentBatchId,
        Long parentBatchPk,
        String childBatchId,
        Long childBatchPk,
        double weightKg,
        LocalDateTime timestamp
) {}
