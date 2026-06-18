package com.opaltrace.platform.custodychain.domain.model.events;

import java.time.LocalDateTime;

public record DelayedTransportAlertEvent(
        String batchId,
        Long batchPk,
        Long supervisorId,
        LocalDateTime lastUpdateTimestamp,
        long delayMinutes
) {}
