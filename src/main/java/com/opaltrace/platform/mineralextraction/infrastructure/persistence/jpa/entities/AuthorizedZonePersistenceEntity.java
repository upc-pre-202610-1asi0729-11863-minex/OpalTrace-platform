package com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.entities;

import com.opaltrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "authorized_zones")
@Getter
@Setter
@NoArgsConstructor
public class AuthorizedZonePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false)
    private String zoneName;

    @Column(nullable = false)
    private double centerLatitude;

    @Column(nullable = false)
    private double centerLongitude;

    @Column(nullable = false)
    private double radiusMeters;

    @Column(nullable = false)
    private boolean active = true;
}
