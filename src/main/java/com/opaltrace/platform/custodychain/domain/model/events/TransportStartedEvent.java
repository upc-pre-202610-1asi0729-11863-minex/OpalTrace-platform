package com.opaltrace.platform.custodychain.domain.model.events;

import java.time.LocalDateTime;

public record TransportStartedEvent(
        String batchId,
        Long batchPk,
        Long supervisorId,
        double latitude,
        double longitude,
        LocalDateTime timestamp
) {}
