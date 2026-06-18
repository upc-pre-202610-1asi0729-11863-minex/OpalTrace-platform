package com.opaltrace.platform.mineralextraction.domain.model.commands;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.AnomalyCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportAnomalyCommand(
        Long batchPk,
        @NotBlank String description,
        @NotNull AnomalyCategory category,
        String photoEvidenceUrl,
        Long reportedByUserId
) {}
