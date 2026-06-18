package com.opaltrace.platform.consumerexperience.interfaces.rest.transform;

import com.opaltrace.platform.consumerexperience.domain.model.aggregates.ProductVerification;
import com.opaltrace.platform.consumerexperience.domain.model.entities.TraceabilityPoint;
import com.opaltrace.platform.consumerexperience.domain.model.valueobjects.VerificationResult;
import com.opaltrace.platform.consumerexperience.interfaces.rest.resources.ProductVerificationResultResource;
import com.opaltrace.platform.consumerexperience.interfaces.rest.resources.TraceabilityPointResource;

public final class ProductVerificationResourceFromEntityAssembler {

    private ProductVerificationResourceFromEntityAssembler() {
    }

    public static ProductVerificationResultResource toResource(ProductVerification verification) {
        String message = verification.getVerificationResult() == VerificationResult.AUTHENTIC
                ? "Producto Auténtico Certificado"
                : "Autenticidad No Verificable";
        return new ProductVerificationResultResource(
                verification.getCertificateId(),
                verification.getVerificationResult(),
                verification.getFailureReason(),
                verification.getProductId(),
                verification.getBatchId(),
                verification.getVerifiedAt(),
                message
        );
    }

    public static TraceabilityPointResource toTraceabilityPointResource(TraceabilityPoint point) {
        return new TraceabilityPointResource(
                point.getEventType(),
                point.getLatitude(),
                point.getLongitude(),
                point.getTimestamp(),
                point.getActorName(),
                point.getBlockchainTxHash(),
                "https://etherscan.io/tx/" + point.getBlockchainTxHash(),
                point.getSequenceOrder()
        );
    }
}
