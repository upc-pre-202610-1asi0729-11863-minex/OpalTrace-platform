package com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.assemblers;

import com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch;
import com.opaltrace.platform.mineralextraction.domain.model.entities.AnomalyReport;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.GpsCoordinate;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.WeightKg;
import com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.entities.AnomalyReportPersistenceEntity;
import com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.entities.MineralBatchPersistenceEntity;

import java.util.List;

public final class MineralBatchPersistenceAssembler {

    private MineralBatchPersistenceAssembler() {}

    public static MineralBatch toDomainFromPersistence(MineralBatchPersistenceEntity entity) {
        if (entity == null) return null;
        var batch = new MineralBatch();
        batch.setId(entity.getId());

        List<AnomalyReport> anomalyReports = entity.getAnomalyReports().stream()
                .map(MineralBatchPersistenceAssembler::toDomainAnomalyFromPersistence)
                .toList();

        batch.reconstitute(
                entity.getBatchId(),
                entity.getMineralType(),
                new WeightKg(entity.getWeightKg()),
                new GpsCoordinate(entity.getOriginLatitude(), entity.getOriginLongitude()),
                entity.getStatus(),
                entity.isBlocked(),
                entity.getSupervisorId(),
                entity.getMiningCompanyId(),
                entity.getBlockchainTxHash(),
                entity.getParentBatchId(),
                entity.getQrCodeData(),
                anomalyReports
        );
        return batch;
    }

    public static MineralBatchPersistenceEntity toPersistenceFromDomain(MineralBatch batch) {
        if (batch == null) return null;
        var entity = new MineralBatchPersistenceEntity();
        if (batch.getId() != null) entity.setId(batch.getId());
        entity.setBatchId(batch.getBatchId());
        entity.setMineralType(batch.getMineralType());
        entity.setWeightKg(batch.getWeight().value());
        entity.setOriginLatitude(batch.getOriginCoordinates().latitude());
        entity.setOriginLongitude(batch.getOriginCoordinates().longitude());
        entity.setStatus(batch.getStatus());
        entity.setBlocked(batch.isBlocked());
        entity.setSupervisorId(batch.getSupervisorId());
        entity.setMiningCompanyId(batch.getMiningCompanyId());
        entity.setBlockchainTxHash(batch.getBlockchainTxHash());
        entity.setParentBatchId(batch.getParentBatchId());
        entity.setQrCodeData(batch.getQrCodeData());

        var anomalyEntities = batch.getAnomalyReports().stream()
                .map(a -> toPersistenceAnomalyFromDomain(a, entity))
                .toList();
        entity.getAnomalyReports().clear();
        entity.getAnomalyReports().addAll(anomalyEntities);

        return entity;
    }

    private static AnomalyReport toDomainAnomalyFromPersistence(AnomalyReportPersistenceEntity entity) {
        var report = new AnomalyReport();
        report.setId(entity.getId());
        report.reconstitute(
                entity.getDescription(),
                entity.getCategory(),
                entity.getPhotoEvidenceUrl(),
                entity.isResolved(),
                entity.getReportedAt(),
                entity.getReportedByUserId()
        );
        return report;
    }

    private static AnomalyReportPersistenceEntity toPersistenceAnomalyFromDomain(
            AnomalyReport report, MineralBatchPersistenceEntity batchEntity) {
        var entity = new AnomalyReportPersistenceEntity();
        if (report.getId() != null) entity.setId(report.getId());
        entity.setBatch(batchEntity);
        entity.setDescription(report.getDescription());
        entity.setCategory(report.getCategory());
        entity.setPhotoEvidenceUrl(report.getPhotoEvidenceUrl());
        entity.setResolved(report.isResolved());
        entity.setReportedAt(report.getReportedAt());
        entity.setReportedByUserId(report.getReportedByUserId());
        return entity;
    }
}
