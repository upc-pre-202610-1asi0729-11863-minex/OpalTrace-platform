package com.opaltrace.platform.analytics.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record GenerateEsgReportResource(
        Long requestedByUserId,
        @NotNull LocalDate periodStart,
        @NotNull LocalDate periodEnd
) {}
