package com.opaltrace.platform.consumerexperience.interfaces.rest.resources;

public record VerificationHistoryItemResource(
        String certId,
        String productName,
        String verifiedAt,
        String result
) {
}
