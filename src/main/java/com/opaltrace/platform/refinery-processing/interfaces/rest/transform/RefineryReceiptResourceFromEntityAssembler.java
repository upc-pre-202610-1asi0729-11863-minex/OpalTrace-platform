package com.opaltrace.platform.refineryprocessing.interfaces.rest.transform;

import com.opaltrace.platform.refineryprocessing.domain.model.aggregates.RefineryReceipt;
import com.opaltrace.platform.refineryprocessing.interfaces.rest.resources.RefineryReceiptResource;
import com.opaltrace.platform.refineryprocessing.interfaces.rest.resources.SubLotRecordResource;

public final class RefineryReceiptResourceFromEntityAssembler {

    private RefineryReceiptResourceFromEntityAssembler() {}

    public static RefineryReceiptResource toResource(RefineryReceipt receipt) {
        var subLotResources = receipt.getSubLots().stream()
                .map(sl -> new SubLotRecordResource(
                        sl.getId(),
                        sl.getParentBatchPk(),
                        sl.getChildBatchPk(),
                        sl.getChildBatchId(),
                        sl.getWeightKg(),
                        sl.getCreatedAt()))
                .toList();
        return new RefineryReceiptResource(
                receipt.getId(),
                receipt.getBatchPk(),
                receipt.getBatchId(),
                receipt.getRefineryId(),
                receipt.getSupervisorId(),
                receipt.getDeclaredWeightKg(),
                receipt.getOriginalWeightKg(),
                receipt.getWeightDiscrepancyPercent(),
                receipt.getReceivedAt(),
                receipt.getProcessingCompletedAt(),
                receipt.getBlockchainTxHash(),
                subLotResources
        );
    }
}
