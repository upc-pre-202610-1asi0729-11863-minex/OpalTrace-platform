package com.opaltrace.platform.custodychain.interfaces.rest.transform;

import com.opaltrace.platform.custodychain.domain.model.aggregates.CustodyTransfer;
import com.opaltrace.platform.custodychain.interfaces.rest.resources.CustodyTransferResource;
import com.opaltrace.platform.custodychain.interfaces.rest.resources.LocationUpdateResource;

public final class CustodyTransferResourceFromEntityAssembler {

    private CustodyTransferResourceFromEntityAssembler() {}

    public static CustodyTransferResource toResource(CustodyTransfer transfer) {
        var locationUpdateResources = transfer.getLocationUpdates().stream()
                .map(lu -> new LocationUpdateResource(
                        lu.getId(),
                        lu.getLatitude(),
                        lu.getLongitude(),
                        lu.getRecordedAt(),
                        lu.getRecordedByUserId()))
                .toList();
        return new CustodyTransferResource(
                transfer.getId(),
                transfer.getBatchId(),
                transfer.getBatchPk(),
                transfer.getCustodyHolderUserId(),
                transfer.getStatus(),
                transfer.getStartLatitude(),
                transfer.getStartLongitude(),
                transfer.getEndLatitude(),
                transfer.getEndLongitude(),
                transfer.getBlockchainTxHash(),
                transfer.getStartedAt(),
                transfer.getCompletedAt(),
                locationUpdateResources
        );
    }
}
