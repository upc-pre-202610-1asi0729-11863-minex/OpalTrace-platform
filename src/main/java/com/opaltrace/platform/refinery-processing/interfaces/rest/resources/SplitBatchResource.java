package com.opaltrace.platform.refineryprocessing.interfaces.rest.resources;

import java.util.List;

public record SplitBatchResource(
        Long supervisorId,
        List<SubLotRequestResource> subLots
) {}
