package com.opaltrace.platform.custodychain.infrastructure.persistence.jpa.repositories;

import com.opaltrace.platform.custodychain.infrastructure.persistence.jpa.entities.CustodyTransferPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CustodyTransferPersistenceRepository extends JpaRepository<CustodyTransferPersistenceEntity, Long> {
    Optional<CustodyTransferPersistenceEntity> findByBatchPk(Long batchPk);
    List<CustodyTransferPersistenceEntity> findByCustodyHolderUserId(Long custodyHolderUserId);
}
