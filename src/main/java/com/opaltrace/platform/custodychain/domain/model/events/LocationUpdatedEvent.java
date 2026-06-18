package com.opaltrace.platform.custodychain.domain.model.events;

import java.time.LocalDateTime;

public record LocationUpdatedEvent(
        String batchId,
        Long batchPk,
        double latitude,
        double longitude,
        Long supervisorId,
        LocalDateTime timestamp
) {}
