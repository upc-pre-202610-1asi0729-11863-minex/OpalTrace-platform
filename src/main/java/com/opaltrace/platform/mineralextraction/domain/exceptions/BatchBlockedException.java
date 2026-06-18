package com.opaltrace.platform.mineralextraction.domain.exceptions;

public class BatchBlockedException extends RuntimeException {
    public BatchBlockedException(String batchId) {
        super("Batch %s is blocked by an active anomaly".formatted(batchId));
    }
}
