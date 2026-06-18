package com.opaltrace.platform.refineryprocessing.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.List;

public record RefineryReceiptResource(
        Long id,
        Long batchPk,
        String batchId,
        Long refineryId,
        Long supervisorId,
        double declaredWeightKg,
        double originalWeightKg,
        double weightDiscrepancyPercent,
        LocalDateTime receivedAt,
        LocalDateTime processingCompletedAt,
        String blockchainTxHash,
        List<SubLotRecordResource> subLots
) {}
