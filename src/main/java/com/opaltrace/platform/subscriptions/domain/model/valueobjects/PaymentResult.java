package com.opaltrace.platform.subscriptions.domain.model.valueobjects;

public record PaymentResult(
        boolean success,
        String transactionId,
        String declineReason) {

    public static PaymentResult approved(String transactionId) {
        return new PaymentResult(true, transactionId, null);
    }

    public static PaymentResult declined(String reason) {
        return new PaymentResult(false, null, reason);
    }
}
