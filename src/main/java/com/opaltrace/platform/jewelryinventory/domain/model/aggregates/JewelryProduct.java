package com.opaltrace.platform.jewelryinventory.domain.model.aggregates;

import com.opaltrace.platform.jewelryinventory.domain.model.commands.ReceiveCertifiedMaterialCommand;
import com.opaltrace.platform.jewelryinventory.domain.model.commands.RegisterExternalMaterialCommand;
import com.opaltrace.platform.jewelryinventory.domain.model.events.CertificateDownloadedEvent;
import com.opaltrace.platform.jewelryinventory.domain.model.events.CertificationGrantedEvent;
import com.opaltrace.platform.jewelryinventory.domain.model.events.CertificationRejectedEvent;
import com.opaltrace.platform.jewelryinventory.domain.model.events.MaterialReceivedEvent;
import com.opaltrace.platform.jewelryinventory.domain.model.valueobjects.CertificationState;
import com.opaltrace.platform.jewelryinventory.domain.model.valueobjects.InventoryCategory;
import com.opaltrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class JewelryProduct extends AbstractDomainAggregateRoot<JewelryProduct> {

    private Long id;
    private String productCode;
    private String name;
    private String description;
    private Long jewelryId;
    private InventoryCategory category;
    private String batchId;
    private Long batchPk;
    private String externalSupplierName;
    private boolean isCertifiedSource;
    private boolean canGenerateCertificate;
    private CertificationState certificationState;
    private String certificateNumber;
    private String blockchainTxHash;
    private LocalDateTime certifiedAt;
    private double weightGrams;

    public JewelryProduct() {
    }

    public JewelryProduct(ReceiveCertifiedMaterialCommand cmd) {
        this.productCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.category = InventoryCategory.CERTIFIED_STOCK;
        this.batchId = cmd.batchId();
        this.batchPk = cmd.batchPk();
        this.isCertifiedSource = true;
        this.canGenerateCertificate = true;
        this.certificationState = CertificationState.PENDING;
        this.jewelryId = cmd.jewelryId();
        this.name = cmd.name();
        this.weightGrams = cmd.weightGrams();
        registerDomainEvent(new MaterialReceivedEvent(cmd.batchId(), null, cmd.jewelryId(), LocalDateTime.now()));
    }

    public JewelryProduct(RegisterExternalMaterialCommand cmd) {
        this.productCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.category = InventoryCategory.EXTERNAL_STOCK;
        this.isCertifiedSource = false;
        this.canGenerateCertificate = false;
        this.certificationState = CertificationState.PENDING;
        this.externalSupplierName = cmd.externalSupplierName();
        this.jewelryId = cmd.jewelryId();
        this.name = cmd.name();
        this.weightGrams = cmd.weightGrams();
        this.description = cmd.description();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void certify(String certificateNumber) {
        this.certificationState = CertificationState.CERTIFIED;
        this.certificateNumber = certificateNumber;
        this.certifiedAt = LocalDateTime.now();
        registerDomainEvent(new CertificationGrantedEvent(this.id, certificateNumber, this.jewelryId, this.batchId, LocalDateTime.now()));
    }

    public void rejectCertification(String reason) {
        this.certificationState = CertificationState.REJECTED;
        registerDomainEvent(new CertificationRejectedEvent(this.id, reason, LocalDateTime.now()));
    }

    public void recordCertificateDownload() {
        registerDomainEvent(new CertificateDownloadedEvent(this.id, this.certificateNumber, LocalDateTime.now()));
    }

    public void reconstitute(Long id, String productCode, String name, String description, Long jewelryId,
                             InventoryCategory category, String batchId, Long batchPk, String externalSupplierName,
                             boolean isCertifiedSource, boolean canGenerateCertificate, CertificationState certificationState,
                             String certificateNumber, String blockchainTxHash, LocalDateTime certifiedAt, double weightGrams) {
        this.id = id;
        this.productCode = productCode;
        this.name = name;
        this.description = description;
        this.jewelryId = jewelryId;
        this.category = category;
        this.batchId = batchId;
        this.batchPk = batchPk;
        this.externalSupplierName = externalSupplierName;
        this.isCertifiedSource = isCertifiedSource;
        this.canGenerateCertificate = canGenerateCertificate;
        this.certificationState = certificationState;
        this.certificateNumber = certificateNumber;
        this.blockchainTxHash = blockchainTxHash;
        this.certifiedAt = certifiedAt;
        this.weightGrams = weightGrams;
    }
}
