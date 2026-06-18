package com.opaltrace.platform.custodychain.application.queryservices;

import com.opaltrace.platform.custodychain.domain.model.aggregates.CustodyTransfer;
import com.opaltrace.platform.custodychain.domain.model.entities.LocationUpdate;
import com.opaltrace.platform.custodychain.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface CustodyChainQueryService {
    Optional<CustodyTransfer> handle(GetCustodyTransferByBatchPkQuery query);
    List<LocationUpdate> handle(GetLocationHistoryByBatchPkQuery query);
    List<CustodyTransfer> handle(GetAllCustodyTransfersQuery query);
    List<CustodyTransfer> handle(GetCustodyTransfersByUserQuery query);
}
