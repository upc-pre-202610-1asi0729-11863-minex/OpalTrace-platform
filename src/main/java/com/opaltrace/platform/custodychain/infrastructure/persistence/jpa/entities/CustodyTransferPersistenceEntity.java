package com.opaltrace.platform.custodychain.infrastructure.persistence.jpa.entities;

import com.opaltrace.platform.custodychain.domain.model.valueobjects.CustodyStatus;
import com.opaltrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "custody_transfers")
@Getter
@Setter
@NoArgsConstructor
public class CustodyTransferPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private String batchId;
    private Long batchPk;
    private Long custodyHolderUserId;

    @Enumerated(EnumType.STRING)
    private CustodyStatus status;

    private Double startLatitude;
    private Double startLongitude;
    private Double endLatitude;
    private Double endLongitude;
    private String blockchainTxHash;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LocationUpdatePersistenceEntity> locationUpdates = new ArrayList<>();
}
