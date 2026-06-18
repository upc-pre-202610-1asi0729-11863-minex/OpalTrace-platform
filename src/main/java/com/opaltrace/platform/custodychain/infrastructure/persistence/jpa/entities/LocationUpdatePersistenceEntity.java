package com.opaltrace.platform.custodychain.infrastructure.persistence.jpa.entities;

import com.opaltrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "location_updates")
@Getter
@Setter
@NoArgsConstructor
public class LocationUpdatePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id", nullable = false)
    private CustodyTransferPersistenceEntity transfer;

    private double latitude;
    private double longitude;
    private LocalDateTime recordedAt;
    private Long recordedByUserId;
}
