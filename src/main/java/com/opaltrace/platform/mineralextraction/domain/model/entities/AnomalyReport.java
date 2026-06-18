package com.opaltrace.platform.mineralextraction.domain.model.entities;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.AnomalyCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class AnomalyReport {

    @Setter
    private Long id;
    private String description;
    private AnomalyCategory category;
    private String photoEvidenceUrl;
    private boolean resolved;
    private LocalDateTime reportedAt;
    private Long reportedByUserId;

    public AnomalyReport() {}

    public AnomalyReport(String description, AnomalyCategory category, String photoEvidenceUrl, Long reportedByUserId) {
        this.description = description;
        this.category = category;
        this.photoEvidenceUrl = photoEvidenceUrl;
        this.resolved = false;
        this.reportedAt = LocalDateTime.now();
        this.reportedByUserId = reportedByUserId;
    }

    public void resolve() {
        this.resolved = true;
    }

    public void reconstitute(String description, AnomalyCategory category, String photoEvidenceUrl,
                             boolean resolved, LocalDateTime reportedAt, Long reportedByUserId) {
        this.description = description;
        this.category = category;
        this.photoEvidenceUrl = photoEvidenceUrl;
        this.resolved = resolved;
        this.reportedAt = reportedAt;
        this.reportedByUserId = reportedByUserId;
    }
}
