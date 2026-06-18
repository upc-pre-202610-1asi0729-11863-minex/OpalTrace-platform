package com.opaltrace.platform.refineryprocessing.infrastructure.persistence.jpa.repositories;

import com.opaltrace.platform.refineryprocessing.infrastructure.persistence.jpa.entities.RefineryReceiptPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefineryReceiptPersistenceRepository extends JpaRepository<RefineryReceiptPersistenceEntity, Long> {
    Optional<RefineryReceiptPersistenceEntity> findByBatchPk(Long batchPk);
    boolean existsByBatchPk(Long batchPk);
}
