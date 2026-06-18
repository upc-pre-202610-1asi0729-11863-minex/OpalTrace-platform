package com.opaltrace.platform.refineryprocessing.infrastructure.persistence.jpa.assemblers;

import com.opaltrace.platform.refineryprocessing.domain.model.aggregates.RefineryReceipt;
import com.opaltrace.platform.refineryprocessing.domain.model.entities.SubLotRecord;
import com.opaltrace.platform.refineryprocessing.infrastructure.persistence.jpa.entities.RefineryReceiptPersistenceEntity;
import com.opaltrace.platform.refineryprocessing.infrastructure.persistence.jpa.entities.SubLotRecordPersistenceEntity;

import java.util.ArrayList;
import java.util.List;

public final class RefineryReceiptPersistenceAssembler {

    private RefineryReceiptPersistenceAssembler() {}

    public static RefineryReceipt toDomainFromPersistence(RefineryReceiptPersistenceEntity entity) {
        var receipt = new RefineryReceipt();
        List<SubLotRecord> subLots = new ArrayList<>();
        for (var slEntity : entity.getSubLots()) {
            var sl = new SubLotRecord();
            sl.reconstitute(slEntity.getId(), slEntity.getParentBatchPk(), slEntity.getChildBatchPk(),
                    slEntity.getChildBatchId(), slEntity.getWeightKg(), slEntity.getCreatedAtRecord());
            subLots.add(sl);
        }
        receipt.reconstitute(
                entity.getId(),
                entity.getBatchPk(),
                entity.getBatchId(),
                entity.getRefineryId(),
                entity.getSupervisorId(),
                entity.getDeclaredWeightKg(),
                entity.getOriginalWeightKg(),
                entity.getWeightDiscrepancyPercent(),
                entity.getReceivedAt(),
                entity.getProcessingCompletedAt(),
                entity.getBlockchainTxHash(),
                subLots
        );
        return receipt;
    }

    public static RefineryReceiptPersistenceEntity toPersistenceFromDomain(RefineryReceipt receipt) {
        var entity = new RefineryReceiptPersistenceEntity();
        if (receipt.getId() != null) entity.setId(receipt.getId());
        entity.setBatchPk(receipt.getBatchPk());
        entity.setBatchId(receipt.getBatchId());
        entity.setRefineryId(receipt.getRefineryId());
        entity.setSupervisorId(receipt.getSupervisorId());
        entity.setDeclaredWeightKg(receipt.getDeclaredWeightKg());
        entity.setOriginalWeightKg(receipt.getOriginalWeightKg());
        entity.setWeightDiscrepancyPercent(receipt.getWeightDiscrepancyPercent());
        entity.setReceivedAt(receipt.getReceivedAt());
        entity.setProcessingCompletedAt(receipt.getProcessingCompletedAt());
        entity.setBlockchainTxHash(receipt.getBlockchainTxHash());

        List<SubLotRecordPersistenceEntity> slEntities = new ArrayList<>();
        for (var sl : receipt.getSubLots()) {
            var slEntity = new SubLotRecordPersistenceEntity();
            if (sl.getId() != null) slEntity.setId(sl.getId());
            slEntity.setReceipt(entity);
            slEntity.setParentBatchPk(sl.getParentBatchPk());
            slEntity.setChildBatchPk(sl.getChildBatchPk());
            slEntity.setChildBatchId(sl.getChildBatchId());
            slEntity.setWeightKg(sl.getWeightKg());
            slEntity.setCreatedAtRecord(sl.getCreatedAt());
            slEntities.add(slEntity);
        }
        entity.setSubLots(slEntities);
        return entity;
    }
}
