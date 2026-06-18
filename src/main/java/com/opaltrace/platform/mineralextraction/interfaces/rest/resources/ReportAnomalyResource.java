package com.opaltrace.platform.mineralextraction.interfaces.rest.resources;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.AnomalyCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReportAnomalyResource(
        @NotBlank String description,
        @NotNull AnomalyCategory category,
        String photoEvidenceUrl,
        Long reportedByUserId
) {}
