package com.opaltrace.platform.analytics.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record GenerateComparativeAnalysisResource(
        Long requestedByUserId,
        @NotNull LocalDate period1Start,
        @NotNull LocalDate period1End,
        @NotNull LocalDate period2Start,
        @NotNull LocalDate period2End
) {}
