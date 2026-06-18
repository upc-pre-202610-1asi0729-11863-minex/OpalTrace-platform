package com.opaltrace.platform.refineryprocessing.infrastructure.persistence.jpa.entities;

import com.opaltrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "refinery_receipts")
@Getter
@Setter
@NoArgsConstructor
public class RefineryReceiptPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long batchPk;

    @Column(length = 20)
    private String batchId;

    private Long refineryId;
    private Long supervisorId;
    private double declaredWeightKg;
    private double originalWeightKg;
    private double weightDiscrepancyPercent;
    private LocalDateTime receivedAt;
    private LocalDateTime processingCompletedAt;

    @Column(length = 100)
    private String blockchainTxHash;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubLotRecordPersistenceEntity> subLots = new ArrayList<>();
}
