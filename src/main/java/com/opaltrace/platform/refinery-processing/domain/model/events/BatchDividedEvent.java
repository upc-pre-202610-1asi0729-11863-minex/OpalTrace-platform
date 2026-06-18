package com.opaltrace.platform.refineryprocessing.domain.model.events;

import java.time.LocalDateTime;

public record BatchDividedEvent(
        String parentBatchId,
        Long parentBatchPk,
        int numberOfSubLots,
        LocalDateTime timestamp
) {}
