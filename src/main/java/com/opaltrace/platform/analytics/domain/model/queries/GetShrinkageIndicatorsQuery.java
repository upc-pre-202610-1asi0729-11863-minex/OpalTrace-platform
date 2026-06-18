package com.opaltrace.platform.analytics.domain.model.queries;

import java.time.LocalDate;

public record GetShrinkageIndicatorsQuery(
        Long requestedByUserId,
        LocalDate fromDate,
        LocalDate toDate
) {}
