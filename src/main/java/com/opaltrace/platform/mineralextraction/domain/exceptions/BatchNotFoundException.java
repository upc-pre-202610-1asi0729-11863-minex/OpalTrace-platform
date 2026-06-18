package com.opaltrace.platform.mineralextraction.domain.exceptions;

public class BatchNotFoundException extends RuntimeException {
    public BatchNotFoundException(Long id) {
        super("MineralBatch not found with id: " + id);
    }

    public BatchNotFoundException(String batchId) {
        super("MineralBatch not found with batchId: " + batchId);
    }
}
