package com.opaltrace.platform.mineralextraction.interfaces.rest.resources;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.AnomalyCategory;

import java.time.LocalDateTime;

public record AnomalyReportResource(
        Long id,
        String description,
        AnomalyCategory category,
        String photoEvidenceUrl,
        boolean resolved,
        LocalDateTime reportedAt,
        Long reportedByUserId
) {}
