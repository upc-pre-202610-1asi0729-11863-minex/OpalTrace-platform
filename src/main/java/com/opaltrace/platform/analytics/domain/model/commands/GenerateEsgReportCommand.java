package com.opaltrace.platform.analytics.domain.model.commands;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record GenerateEsgReportCommand(
        Long requestedByUserId,
        @NotNull LocalDate periodStart,
        @NotNull LocalDate periodEnd
) {}
