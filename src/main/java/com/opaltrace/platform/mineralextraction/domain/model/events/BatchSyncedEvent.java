package com.opaltrace.platform.mineralextraction.domain.model.events;

import java.time.LocalDateTime;

public record BatchSyncedEvent(
        String batchId,
        Long supervisorId,
        LocalDateTime syncedAt
) {}
