package com.opaltrace.platform.custodychain.application.internal.queryservices;

import com.opaltrace.platform.custodychain.application.queryservices.CustodyChainQueryService;
import com.opaltrace.platform.custodychain.domain.model.aggregates.CustodyTransfer;
import com.opaltrace.platform.custodychain.domain.model.entities.LocationUpdate;
import com.opaltrace.platform.custodychain.domain.model.queries.*;
import com.opaltrace.platform.custodychain.domain.repositories.CustodyTransferRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CustodyChainQueryServiceImpl implements CustodyChainQueryService {

    private final CustodyTransferRepository custodyTransferRepository;

    public CustodyChainQueryServiceImpl(CustodyTransferRepository custodyTransferRepository) {
        this.custodyTransferRepository = custodyTransferRepository;
    }

    @Override
    public Optional<CustodyTransfer> handle(GetCustodyTransferByBatchPkQuery query) {
        return custodyTransferRepository.findByBatchPk(query.batchPk());
    }

    @Override
    public List<LocationUpdate> handle(GetLocationHistoryByBatchPkQuery query) {
        return custodyTransferRepository.findByBatchPk(query.batchPk())
                .map(CustodyTransfer::getLocationUpdates)
                .orElse(Collections.emptyList());
    }

    @Override
    public List<CustodyTransfer> handle(GetAllCustodyTransfersQuery query) {
        return custodyTransferRepository.findAll();
    }

    @Override
    public List<CustodyTransfer> handle(GetCustodyTransfersByUserQuery query) {
        return custodyTransferRepository.findByCustodyHolderUserId(query.userId());
    }
}
