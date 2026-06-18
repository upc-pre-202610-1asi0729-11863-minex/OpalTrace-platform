package com.opaltrace.platform.refineryprocessing.domain.model.commands;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.MineralType;

import java.util.List;

public record SplitBatchCommand(
        Long parentBatchPk,
        Long supervisorId,
        List<SubLotRequest> subLots
) {
    public record SubLotRequest(double weightKg, MineralType mineralType) {}
}
