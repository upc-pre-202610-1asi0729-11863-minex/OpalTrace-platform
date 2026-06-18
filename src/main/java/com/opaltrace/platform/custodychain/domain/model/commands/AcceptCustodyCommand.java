package com.opaltrace.platform.custodychain.domain.model.commands;

public record AcceptCustodyCommand(
        Long batchPk,
        String batchId,
        Long custodyHolderUserId,
        Double latitude,
        Double longitude
) {}
