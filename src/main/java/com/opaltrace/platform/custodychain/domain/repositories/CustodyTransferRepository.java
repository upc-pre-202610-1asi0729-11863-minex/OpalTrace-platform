package com.opaltrace.platform.custodychain.domain.repositories;

import com.opaltrace.platform.custodychain.domain.model.aggregates.CustodyTransfer;

import java.util.List;
import java.util.Optional;

public interface CustodyTransferRepository {
    CustodyTransfer save(CustodyTransfer transfer);
    Optional<CustodyTransfer> findByBatchPk(Long batchPk);
    List<CustodyTransfer> findAll();
    List<CustodyTransfer> findByCustodyHolderUserId(Long userId);
}
