package com.opaltrace.platform.jewelryinventory.interfaces.rest.transform;

import com.opaltrace.platform.jewelryinventory.domain.model.aggregates.JewelryProduct;
import com.opaltrace.platform.jewelryinventory.interfaces.rest.resources.CertificateResource;
import com.opaltrace.platform.jewelryinventory.interfaces.rest.resources.JewelryProductResource;

public final class JewelryProductResourceFromEntityAssembler {

    private JewelryProductResourceFromEntityAssembler() {
    }

    public static JewelryProductResource toResource(JewelryProduct product) {
        return new JewelryProductResource(
                product.getId(),
                product.getProductCode(),
                product.getName(),
                product.getDescription(),
                product.getJewelryId(),
                product.getCategory(),
                product.getBatchId(),
                product.getBatchPk(),
                product.getExternalSupplierName(),
                product.isCertifiedSource(),
                product.isCanGenerateCertificate(),
                product.getCertificationState(),
                product.getCertificateNumber(),
                product.getCertifiedAt(),
                product.getWeightGrams()
        );
    }

    public static CertificateResource toCertificateResource(JewelryProduct product) {
        return new CertificateResource(
                product.getId(),
                product.getCertificateNumber(),
                product.getName(),
                product.getBatchId(),
                product.getJewelryId(),
                product.getCertifiedAt(),
                product.getWeightGrams(),
                "https://opaltrace.com/verify/" + product.getCertificateNumber()
        );
    }
}
