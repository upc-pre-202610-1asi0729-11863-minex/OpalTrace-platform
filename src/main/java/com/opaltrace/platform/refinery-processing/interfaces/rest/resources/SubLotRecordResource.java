package com.opaltrace.platform.refineryprocessing.interfaces.rest.resources;

import java.time.LocalDateTime;

public record SubLotRecordResource(
        Long id,
        Long parentBatchPk,
        Long childBatchPk,
        String childBatchId,
        double weightKg,
        LocalDateTime createdAt
) {}
