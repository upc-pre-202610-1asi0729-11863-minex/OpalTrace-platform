package com.opaltrace.platform.consumerexperience.interfaces.rest.resources;

import java.time.LocalDateTime;

public record TraceabilityPointResource(
        String eventType,
        Double latitude,
        Double longitude,
        LocalDateTime timestamp,
        String actorName,
        String blockchainTxHash,
        String blockchainExplorerUrl,
        int sequenceOrder
) {
}
