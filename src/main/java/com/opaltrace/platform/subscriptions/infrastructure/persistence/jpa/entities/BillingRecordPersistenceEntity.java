package com.opaltrace.platform.subscriptions.infrastructure.persistence.jpa.entities;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "billing_records")
@Getter
@Setter
@NoArgsConstructor
public class BillingRecordPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", insertable = false, updatable = false)
    private SubscriptionPersistenceEntity subscription;

    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "invoice_number", length = 30, unique = true, nullable = false)
    private String invoiceNumber;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_tier", nullable = false, length = 20)
    private PlanTier planTier;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
}
