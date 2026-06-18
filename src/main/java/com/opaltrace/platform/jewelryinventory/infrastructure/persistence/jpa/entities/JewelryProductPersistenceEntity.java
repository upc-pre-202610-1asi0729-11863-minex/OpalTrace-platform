package com.opaltrace.platform.jewelryinventory.infrastructure.persistence.jpa.entities;

import com.opaltrace.platform.jewelryinventory.domain.model.valueobjects.CertificationState;
import com.opaltrace.platform.jewelryinventory.domain.model.valueobjects.InventoryCategory;
import com.opaltrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "jewelry_products")
@Getter
@Setter
@NoArgsConstructor
public class JewelryProductPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "product_code", length = 50, nullable = false)
    private String productCode;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "jewelry_id")
    private Long jewelryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 30, nullable = false)
    private InventoryCategory category;

    @Column(name = "batch_id", length = 20)
    private String batchId;

    @Column(name = "batch_pk")
    private Long batchPk;

    @Column(name = "external_supplier_name", length = 255)
    private String externalSupplierName;

    @Column(name = "is_certified_source", nullable = false)
    private boolean isCertifiedSource;

    @Column(name = "can_generate_certificate", nullable = false)
    private boolean canGenerateCertificate;

    @Enumerated(EnumType.STRING)
    @Column(name = "certification_state", length = 20, nullable = false)
    private CertificationState certificationState;

    @Column(name = "certificate_number", length = 30, unique = true)
    private String certificateNumber;

    @Column(name = "blockchain_tx_hash", length = 100)
    private String blockchainTxHash;

    @Column(name = "certified_at")
    private LocalDateTime certifiedAt;

    @Column(name = "weight_grams", nullable = false)
    private double weightGrams;
}
