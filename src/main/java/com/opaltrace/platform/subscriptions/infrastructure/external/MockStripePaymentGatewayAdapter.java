package com.opaltrace.platform.subscriptions.infrastructure.external;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.subscriptions.application.PaymentGatewayService;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.PaymentResult;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class MockStripePaymentGatewayAdapter implements PaymentGatewayService {

    private static final Set<String> DECLINED_TOKENS = Set.of(
            "tok_declined",
            "tok_insufficient_funds",
            "tok_card_error"
    );

    @Override
    public PaymentResult charge(String paymentMethodToken, double amount, PlanTier planTier) {
        if (paymentMethodToken == null || paymentMethodToken.isBlank()) {
            return PaymentResult.declined("No payment method provided");
        }
        if (DECLINED_TOKENS.contains(paymentMethodToken)) {
            return PaymentResult.declined("Card declined by issuing bank");
        }
        String transactionId = "txn_mock_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        return PaymentResult.approved(transactionId);
    }
}
