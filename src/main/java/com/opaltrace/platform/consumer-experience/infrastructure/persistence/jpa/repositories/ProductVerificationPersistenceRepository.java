package com.opaltrace.platform.consumerexperience.infrastructure.persistence.jpa.repositories;

import com.opaltrace.platform.consumerexperience.domain.model.valueobjects.VerificationResult;
import com.opaltrace.platform.consumerexperience.infrastructure.persistence.jpa.entities.ProductVerificationPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductVerificationPersistenceRepository extends JpaRepository<ProductVerificationPersistenceEntity, Long> {

    Optional<ProductVerificationPersistenceEntity> findByCertificateId(String certificateId);

    @Query("SELECT COUNT(v) FROM ProductVerificationPersistenceEntity v WHERE v.certificateId = :certId AND v.verifiedAt >= :since")
    long countRecentVerifications(@Param("certId") String certId, @Param("since") LocalDateTime since);

    List<ProductVerificationPersistenceEntity> findByConsumerIdAndVerificationResult(Long consumerId, VerificationResult verificationResult);

    List<ProductVerificationPersistenceEntity> findByConsumerId(Long consumerId);
}
