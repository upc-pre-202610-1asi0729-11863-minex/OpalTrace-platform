package com.opaltrace.platform.subscriptions.interfaces.rest.transform;

import com.opaltrace.platform.subscriptions.domain.model.aggregates.Subscription;
import com.opaltrace.platform.subscriptions.domain.model.entities.BillingRecord;
import com.opaltrace.platform.subscriptions.interfaces.rest.resources.BillingRecordResource;
import com.opaltrace.platform.subscriptions.interfaces.rest.resources.InvoiceResource;
import com.opaltrace.platform.subscriptions.interfaces.rest.resources.SubscriptionResource;

public final class SubscriptionResourceFromEntityAssembler {

    private SubscriptionResourceFromEntityAssembler() {}

    public static SubscriptionResource toResource(Subscription subscription) {
        return new SubscriptionResource(
                subscription.getId(),
                subscription.getUserId(),
                subscription.getPlanTier(),
                subscription.getStatus(),
                subscription.getBillingCycle(),
                subscription.getStartDate(),
                subscription.getEndDate(),
                subscription.getNextBillingDate(),
                subscription.isPendingDowngrade(),
                subscription.getTargetDowngradeTier(),
                subscription.getDowngradeEffectiveDate(),
                subscription.getCancelEffectiveDate()
        );
    }

    public static BillingRecordResource toBillingResource(BillingRecord record) {
        return new BillingRecordResource(
                record.getId(),
                record.getInvoiceNumber(),
                record.getAmount(),
                record.getPlanTier(),
                record.getPaymentStatus(),
                record.getPaymentMethod(),
                record.getTransactionDate()
        );
    }

    public static InvoiceResource toInvoiceResource(BillingRecord record, Long userId) {
        double totalAmount = record.getAmount();
        double baseAmount = totalAmount / 1.18;
        double taxes = totalAmount * 0.18 / 1.18;
        return new InvoiceResource(
                record.getInvoiceNumber(),
                userId,
                record.getPlanTier(),
                record.getAmount(),
                record.getPaymentStatus(),
                record.getTransactionDate(),
                baseAmount,
                taxes,
                totalAmount
        );
    }
}
