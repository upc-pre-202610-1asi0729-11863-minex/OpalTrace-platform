package com.opaltrace.platform.subscriptions.interfaces.rest.resources;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.PaymentStatus;

import java.time.LocalDateTime;

public record BillingRecordResource(
        Long id,
        String invoiceNumber,
        double amount,
        PlanTier planTier,
        PaymentStatus paymentStatus,
        String paymentMethod,
        LocalDateTime transactionDate
) {}
