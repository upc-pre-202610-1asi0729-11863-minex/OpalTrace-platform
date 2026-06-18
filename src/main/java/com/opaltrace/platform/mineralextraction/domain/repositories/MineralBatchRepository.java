package com.opaltrace.platform.mineralextraction.domain.repositories;

import com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.BatchStatus;

import java.util.List;
import java.util.Optional;

public interface MineralBatchRepository {
    MineralBatch save(MineralBatch batch);
    Optional<MineralBatch> findById(Long id);
    Optional<MineralBatch> findByBatchId(String batchId);
    List<MineralBatch> findAll();
    List<MineralBatch> findByMiningCompanyId(Long miningCompanyId);
    List<MineralBatch> findByStatus(BatchStatus status);
    boolean existsByBatchId(String batchId);
    int countByYear(int year);
}
