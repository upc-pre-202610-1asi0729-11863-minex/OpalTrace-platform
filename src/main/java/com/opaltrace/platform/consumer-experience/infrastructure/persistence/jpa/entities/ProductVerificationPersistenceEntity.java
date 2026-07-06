package com.opaltrace.platform.consumerexperience.infrastructure.persistence.jpa.entities;

import com.opaltrace.platform.consumerexperience.domain.model.valueobjects.VerificationResult;
import com.opaltrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_verifications")
@Getter
@Setter
@NoArgsConstructor
public class ProductVerificationPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "certificate_id", length = 50, nullable = false)
    private String certificateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_result", length = 20, nullable = false)
    private VerificationResult verificationResult;

    @Column(name = "failure_reason", length = 500)
    private String failureReason;

    @Column(name = "verifier_ip", length = 50)
    private String verifierIp;

    @Column(name = "verified_at", nullable = false)
    private LocalDateTime verifiedAt;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "batch_id", length = 20)
    private String batchId;

    @Column(name = "consumer_id")
    private Long consumerId;
}
