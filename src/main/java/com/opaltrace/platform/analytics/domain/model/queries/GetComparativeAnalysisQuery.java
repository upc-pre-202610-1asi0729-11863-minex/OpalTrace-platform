package com.opaltrace.platform.analytics.domain.model.queries;

import java.time.LocalDate;

public record GetComparativeAnalysisQuery(
        Long requestedByUserId,
        LocalDate period1Start,
        LocalDate period1End,
        LocalDate period2Start,
        LocalDate period2End
) {}
