package com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.entities;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.AnomalyCategory;
import com.opaltrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "anomaly_reports")
@Getter
@Setter
@NoArgsConstructor
public class AnomalyReportPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private MineralBatchPersistenceEntity batch;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnomalyCategory category;

    @Column(length = 500)
    private String photoEvidenceUrl;

    @Column(nullable = false)
    private boolean resolved = false;

    @Column(nullable = false)
    private LocalDateTime reportedAt;

    @Column
    private Long reportedByUserId;
}
