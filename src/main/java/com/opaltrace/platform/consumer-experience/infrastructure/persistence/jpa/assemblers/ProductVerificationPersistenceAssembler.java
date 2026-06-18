package com.opaltrace.platform.consumerexperience.infrastructure.persistence.jpa.assemblers;

import com.opaltrace.platform.consumerexperience.domain.model.aggregates.ProductVerification;
import com.opaltrace.platform.consumerexperience.infrastructure.persistence.jpa.entities.ProductVerificationPersistenceEntity;

public final class ProductVerificationPersistenceAssembler {

    private ProductVerificationPersistenceAssembler() {
    }

    public static ProductVerification toDomain(ProductVerificationPersistenceEntity entity) {
        var verification = new ProductVerification();
        verification.reconstitute(
                entity.getId(),
                entity.getCertificateId(),
                entity.getVerificationResult(),
                entity.getFailureReason(),
                entity.getVerifierIp(),
                entity.getVerifiedAt(),
                entity.getProductId(),
                entity.getBatchId()
        );
        return verification;
    }

    public static ProductVerificationPersistenceEntity toPersistence(ProductVerification verification) {
        var entity = new ProductVerificationPersistenceEntity();
        if (verification.getId() != null) {
            entity.setId(verification.getId());
        }
        entity.setCertificateId(verification.getCertificateId());
        entity.setVerificationResult(verification.getVerificationResult());
        entity.setFailureReason(verification.getFailureReason());
        entity.setVerifierIp(verification.getVerifierIp());
        entity.setVerifiedAt(verification.getVerifiedAt());
        entity.setProductId(verification.getProductId());
        entity.setBatchId(verification.getBatchId());
        return entity;
    }
}
