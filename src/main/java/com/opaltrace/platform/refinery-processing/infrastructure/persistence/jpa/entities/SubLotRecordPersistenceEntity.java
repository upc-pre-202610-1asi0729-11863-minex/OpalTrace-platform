package com.opaltrace.platform.refineryprocessing.infrastructure.persistence.jpa.entities;

import com.opaltrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sub_lot_records")
@Getter
@Setter
@NoArgsConstructor
public class SubLotRecordPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    private RefineryReceiptPersistenceEntity receipt;

    private Long parentBatchPk;
    private Long childBatchPk;

    @Column(length = 30)
    private String childBatchId;

    private double weightKg;
    private LocalDateTime createdAtRecord;
}
