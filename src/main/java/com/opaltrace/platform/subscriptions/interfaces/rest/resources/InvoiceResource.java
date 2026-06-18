package com.opaltrace.platform.subscriptions.interfaces.rest.resources;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.PaymentStatus;

import java.time.LocalDateTime;

public record InvoiceResource(
        String invoiceNumber,
        Long userId,
        PlanTier planTier,
        double amount,
        PaymentStatus paymentStatus,
        LocalDateTime transactionDate,
        double baseAmount,
        double taxes,
        double totalAmount
) {}
