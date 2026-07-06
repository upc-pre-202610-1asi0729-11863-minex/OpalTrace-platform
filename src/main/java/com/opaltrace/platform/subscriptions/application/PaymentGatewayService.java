package com.opaltrace.platform.subscriptions.application;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.PaymentResult;

public interface PaymentGatewayService {

    PaymentResult charge(String paymentMethodToken, double amount, PlanTier planTier);
}
