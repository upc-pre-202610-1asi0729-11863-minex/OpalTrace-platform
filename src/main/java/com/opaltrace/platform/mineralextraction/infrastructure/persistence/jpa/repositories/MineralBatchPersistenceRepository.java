package com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.repositories;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.BatchStatus;
import com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.entities.MineralBatchPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MineralBatchPersistenceRepository extends JpaRepository<MineralBatchPersistenceEntity, Long> {
    Optional<MineralBatchPersistenceEntity> findByBatchId(String batchId);
    List<MineralBatchPersistenceEntity> findByMiningCompanyId(Long miningCompanyId);
    List<MineralBatchPersistenceEntity> findByStatus(BatchStatus status);
    boolean existsByBatchId(String batchId);

    @Query("SELECT COUNT(b) FROM MineralBatchPersistenceEntity b WHERE YEAR(b.createdAt) = :year")
    int countByYear(@Param("year") int year);
}
