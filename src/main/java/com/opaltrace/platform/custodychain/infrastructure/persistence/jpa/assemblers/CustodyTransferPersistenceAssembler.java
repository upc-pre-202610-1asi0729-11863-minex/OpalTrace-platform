package com.opaltrace.platform.custodychain.infrastructure.persistence.jpa.assemblers;

import com.opaltrace.platform.custodychain.domain.model.aggregates.CustodyTransfer;
import com.opaltrace.platform.custodychain.domain.model.entities.LocationUpdate;
import com.opaltrace.platform.custodychain.infrastructure.persistence.jpa.entities.CustodyTransferPersistenceEntity;
import com.opaltrace.platform.custodychain.infrastructure.persistence.jpa.entities.LocationUpdatePersistenceEntity;

import java.util.ArrayList;
import java.util.List;

public final class CustodyTransferPersistenceAssembler {

    private CustodyTransferPersistenceAssembler() {}

    public static CustodyTransfer toDomainFromPersistence(CustodyTransferPersistenceEntity entity) {
        var transfer = new CustodyTransfer();
        List<LocationUpdate> updates = new ArrayList<>();
        for (var luEntity : entity.getLocationUpdates()) {
            var lu = new LocationUpdate();
            lu.reconstitute(luEntity.getId(), luEntity.getLatitude(), luEntity.getLongitude(),
                    luEntity.getRecordedAt(), luEntity.getRecordedByUserId());
            updates.add(lu);
        }
        transfer.reconstitute(
                entity.getId(),
                entity.getBatchId(),
                entity.getBatchPk(),
                entity.getCustodyHolderUserId(),
                entity.getStatus(),
                entity.getStartLatitude(),
                entity.getStartLongitude(),
                entity.getEndLatitude(),
                entity.getEndLongitude(),
                entity.getBlockchainTxHash(),
                updates,
                entity.getStartedAt(),
                entity.getCompletedAt()
        );
        return transfer;
    }

    public static CustodyTransferPersistenceEntity toPersistenceFromDomain(CustodyTransfer transfer) {
        var entity = new CustodyTransferPersistenceEntity();
        if (transfer.getId() != null) entity.setId(transfer.getId());
        entity.setBatchId(transfer.getBatchId());
        entity.setBatchPk(transfer.getBatchPk());
        entity.setCustodyHolderUserId(transfer.getCustodyHolderUserId());
        entity.setStatus(transfer.getStatus());
        entity.setStartLatitude(transfer.getStartLatitude());
        entity.setStartLongitude(transfer.getStartLongitude());
        entity.setEndLatitude(transfer.getEndLatitude());
        entity.setEndLongitude(transfer.getEndLongitude());
        entity.setBlockchainTxHash(transfer.getBlockchainTxHash());
        entity.setStartedAt(transfer.getStartedAt());
        entity.setCompletedAt(transfer.getCompletedAt());

        List<LocationUpdatePersistenceEntity> luEntities = new ArrayList<>();
        for (var lu : transfer.getLocationUpdates()) {
            var luEntity = new LocationUpdatePersistenceEntity();
            if (lu.getId() != null) luEntity.setId(lu.getId());
            luEntity.setTransfer(entity);
            luEntity.setLatitude(lu.getLatitude());
            luEntity.setLongitude(lu.getLongitude());
            luEntity.setRecordedAt(lu.getRecordedAt());
            luEntity.setRecordedByUserId(lu.getRecordedByUserId());
            luEntities.add(luEntity);
        }
        entity.setLocationUpdates(luEntities);
        return entity;
    }
}
