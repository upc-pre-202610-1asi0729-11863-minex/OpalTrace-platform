package com.opaltrace.platform.refineryprocessing.domain.repositories;

import com.opaltrace.platform.refineryprocessing.domain.model.aggregates.RefineryReceipt;

import java.util.List;
import java.util.Optional;

public interface RefineryReceiptRepository {
    RefineryReceipt save(RefineryReceipt receipt);
    Optional<RefineryReceipt> findByBatchPk(Long batchPk);
    List<RefineryReceipt> findAll();
    boolean existsByBatchPk(Long batchPk);
}
