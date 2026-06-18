package com.opaltrace.platform.custodychain.interfaces.rest.resources;

import com.opaltrace.platform.custodychain.domain.model.valueobjects.CustodyStatus;

import java.time.LocalDateTime;
import java.util.List;

public record CustodyTransferResource(
        Long id,
        String batchId,
        Long batchPk,
        Long custodyHolderUserId,
        CustodyStatus status,
        Double startLatitude,
        Double startLongitude,
        Double endLatitude,
        Double endLongitude,
        String blockchainTxHash,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        List<LocationUpdateResource> locationUpdates
) {}
