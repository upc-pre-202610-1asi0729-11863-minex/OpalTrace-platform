package com.opaltrace.platform.consumerexperience.infrastructure.persistence.jpa.adapters;

import com.opaltrace.platform.consumerexperience.domain.model.aggregates.ProductVerification;
import com.opaltrace.platform.consumerexperience.domain.model.valueobjects.VerificationResult;
import com.opaltrace.platform.consumerexperience.domain.repositories.ProductVerificationRepository;
import com.opaltrace.platform.consumerexperience.infrastructure.persistence.jpa.assemblers.ProductVerificationPersistenceAssembler;
import com.opaltrace.platform.consumerexperience.infrastructure.persistence.jpa.repositories.ProductVerificationPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductVerificationRepositoryImpl implements ProductVerificationRepository {

    private final ProductVerificationPersistenceRepository persistenceRepository;

    public ProductVerificationRepositoryImpl(ProductVerificationPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public ProductVerification save(ProductVerification verification) {
        var entity = ProductVerificationPersistenceAssembler.toPersistence(verification);
        var saved = persistenceRepository.save(entity);
        return ProductVerificationPersistenceAssembler.toDomain(saved);
    }

    @Override
    public Optional<ProductVerification> findByCertificateId(String certificateId) {
        return persistenceRepository.findByCertificateId(certificateId)
                .map(ProductVerificationPersistenceAssembler::toDomain);
    }

    @Override
    public List<ProductVerification> findAll() {
        return persistenceRepository.findAll().stream()
                .map(ProductVerificationPersistenceAssembler::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countRecentVerifications(String certificateId, LocalDateTime since) {
        return persistenceRepository.countRecentVerifications(certificateId, since);
    }

    @Override
    public List<ProductVerification> findByConsumerIdAndVerificationResult(Long consumerId, VerificationResult verificationResult) {
        return persistenceRepository.findByConsumerIdAndVerificationResult(consumerId, verificationResult).stream()
                .map(ProductVerificationPersistenceAssembler::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductVerification> findByConsumerId(Long consumerId) {
        return persistenceRepository.findByConsumerId(consumerId).stream()
                .map(ProductVerificationPersistenceAssembler::toDomain)
                .collect(Collectors.toList());
    }
}
