package com.opaltrace.platform.consumerexperience.domain.model.aggregates;

import com.opaltrace.platform.consumerexperience.domain.model.commands.VerifyProductAuthenticityCommand;
import com.opaltrace.platform.consumerexperience.domain.model.events.AuthenticityVerifiedEvent;
import com.opaltrace.platform.consumerexperience.domain.model.valueobjects.VerificationResult;
import com.opaltrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProductVerification extends AbstractDomainAggregateRoot<ProductVerification> {

    private Long id;
    private String certificateId;
    private VerificationResult verificationResult;
    private String failureReason;
    private String verifierIp;
    private LocalDateTime verifiedAt;
    private Long productId;
    private String batchId;
    private Long consumerId;

    public ProductVerification() {
    }

    public ProductVerification(VerifyProductAuthenticityCommand cmd, VerificationResult result,
                                String failureReason, Long productId, String batchId) {
        this.certificateId = cmd.certificateId();
        this.verificationResult = result;
        this.failureReason = failureReason;
        this.verifierIp = cmd.verifierIp();
        this.verifiedAt = LocalDateTime.now();
        this.productId = productId;
        this.batchId = batchId;
        this.consumerId = cmd.consumerId();
        registerDomainEvent(new AuthenticityVerifiedEvent(
                cmd.certificateId(), result, productId, cmd.verifierIp(), this.verifiedAt));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void reconstitute(Long id, String certificateId, VerificationResult verificationResult,
                             String failureReason, String verifierIp, LocalDateTime verifiedAt,
                             Long productId, String batchId, Long consumerId) {
        this.id = id;
        this.certificateId = certificateId;
        this.verificationResult = verificationResult;
        this.failureReason = failureReason;
        this.verifierIp = verifierIp;
        this.verifiedAt = verifiedAt;
        this.productId = productId;
        this.batchId = batchId;
        this.consumerId = consumerId;
    }
}
