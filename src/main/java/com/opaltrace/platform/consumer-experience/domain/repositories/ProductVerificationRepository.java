package com.opaltrace.platform.consumerexperience.domain.repositories;

import com.opaltrace.platform.consumerexperience.domain.model.aggregates.ProductVerification;
import com.opaltrace.platform.consumerexperience.domain.model.valueobjects.VerificationResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductVerificationRepository {
    ProductVerification save(ProductVerification verification);
    Optional<ProductVerification> findByCertificateId(String certificateId);
    List<ProductVerification> findAll();
    long countRecentVerifications(String certificateId, LocalDateTime since);
    List<ProductVerification> findByConsumerIdAndVerificationResult(Long consumerId, VerificationResult verificationResult);
    List<ProductVerification> findByConsumerId(Long consumerId);
}
