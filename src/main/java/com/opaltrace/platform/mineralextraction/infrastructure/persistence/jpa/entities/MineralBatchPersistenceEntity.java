package com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.entities;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.BatchStatus;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.MineralType;
import com.opaltrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mineral_batches")
@Getter
@Setter
@NoArgsConstructor
public class MineralBatchPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String batchId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MineralType mineralType;

    @Column(nullable = false)
    private double weightKg;

    @Column(nullable = false)
    private double originLatitude;

    @Column(nullable = false)
    private double originLongitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatchStatus status;

    @Column(nullable = false)
    private boolean blocked = false;

    @Column
    private Long supervisorId;

    @Column
    private Long miningCompanyId;

    @Column(length = 100)
    private String blockchainTxHash;

    @Column
    private Long parentBatchId;

    @Column(length = 500)
    private String qrCodeData;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnomalyReportPersistenceEntity> anomalyReports = new ArrayList<>();
}
