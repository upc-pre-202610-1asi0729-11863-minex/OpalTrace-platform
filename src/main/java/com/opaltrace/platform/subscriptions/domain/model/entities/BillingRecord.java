package com.opaltrace.platform.subscriptions.domain.model.entities;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.PaymentStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BillingRecord {

    private Long id;
    private Long subscriptionId;
    private String invoiceNumber;
    private double amount;
    private PlanTier planTier;
    private PaymentStatus paymentStatus;
    private String paymentMethod;
    private LocalDateTime transactionDate;

    public BillingRecord() {}

    public BillingRecord(Long subscriptionId, String invoiceNumber, double amount,
                         PlanTier planTier, PaymentStatus paymentStatus, String paymentMethod) {
        this.subscriptionId = subscriptionId;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
        this.planTier = planTier;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.transactionDate = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void reconstitute(Long subscriptionId, String invoiceNumber, double amount,
                             PlanTier planTier, PaymentStatus paymentStatus, String paymentMethod,
                             LocalDateTime transactionDate) {
        this.subscriptionId = subscriptionId;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
        this.planTier = planTier;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.transactionDate = transactionDate;
    }
}
