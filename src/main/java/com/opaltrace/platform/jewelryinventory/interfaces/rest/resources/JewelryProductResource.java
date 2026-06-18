package com.opaltrace.platform.jewelryinventory.interfaces.rest.resources;

import com.opaltrace.platform.jewelryinventory.domain.model.valueobjects.CertificationState;
import com.opaltrace.platform.jewelryinventory.domain.model.valueobjects.InventoryCategory;

import java.time.LocalDateTime;

public record JewelryProductResource(
        Long id,
        String productCode,
        String name,
        String description,
        Long jewelryId,
        InventoryCategory category,
        String batchId,
        Long batchPk,
        String externalSupplierName,
        boolean isCertifiedSource,
        boolean canGenerateCertificate,
        CertificationState certificationState,
        String certificateNumber,
        LocalDateTime certifiedAt,
        double weightGrams
) {
}
