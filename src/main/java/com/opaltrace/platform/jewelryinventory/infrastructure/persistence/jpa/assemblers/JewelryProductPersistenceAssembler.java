package com.opaltrace.platform.jewelryinventory.infrastructure.persistence.jpa.assemblers;

import com.opaltrace.platform.jewelryinventory.domain.model.aggregates.JewelryProduct;
import com.opaltrace.platform.jewelryinventory.infrastructure.persistence.jpa.entities.JewelryProductPersistenceEntity;

public final class JewelryProductPersistenceAssembler {

    private JewelryProductPersistenceAssembler() {
    }

    public static JewelryProduct toDomain(JewelryProductPersistenceEntity entity) {
        var product = new JewelryProduct();
        product.reconstitute(
                entity.getId(),
                entity.getProductCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getJewelryId(),
                entity.getCategory(),
                entity.getBatchId(),
                entity.getBatchPk(),
                entity.getExternalSupplierName(),
                entity.isCertifiedSource(),
                entity.isCanGenerateCertificate(),
                entity.getCertificationState(),
                entity.getCertificateNumber(),
                entity.getBlockchainTxHash(),
                entity.getCertifiedAt(),
                entity.getWeightGrams()
        );
        return product;
    }

    public static JewelryProductPersistenceEntity toPersistence(JewelryProduct product) {
        var entity = new JewelryProductPersistenceEntity();
        if (product.getId() != null) {
            entity.setId(product.getId());
        }
        entity.setProductCode(product.getProductCode());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setJewelryId(product.getJewelryId());
        entity.setCategory(product.getCategory());
        entity.setBatchId(product.getBatchId());
        entity.setBatchPk(product.getBatchPk());
        entity.setExternalSupplierName(product.getExternalSupplierName());
        entity.setCertifiedSource(product.isCertifiedSource());
        entity.setCanGenerateCertificate(product.isCanGenerateCertificate());
        entity.setCertificationState(product.getCertificationState());
        entity.setCertificateNumber(product.getCertificateNumber());
        entity.setBlockchainTxHash(product.getBlockchainTxHash());
        entity.setCertifiedAt(product.getCertifiedAt());
        entity.setWeightGrams(product.getWeightGrams());
        return entity;
    }
}
