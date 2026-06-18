package com.opaltrace.platform.refineryprocessing.domain.model.commands;

public record ReceiveBatchAtRefineryCommand(
        Long batchPk,
        String batchId,
        Long refineryId,
        Long supervisorId,
        double declaredWeightKg
) {}
