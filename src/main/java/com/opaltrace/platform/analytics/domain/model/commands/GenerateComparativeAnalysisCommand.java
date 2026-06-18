package com.opaltrace.platform.analytics.domain.model.commands;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record GenerateComparativeAnalysisCommand(
        Long requestedByUserId,
        @NotNull LocalDate period1Start,
        @NotNull LocalDate period1End,
        @NotNull LocalDate period2Start,
        @NotNull LocalDate period2End
) {}
