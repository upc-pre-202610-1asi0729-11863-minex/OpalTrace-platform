package com.opaltrace.platform.subscriptions.infrastructure.persistence.jpa.assemblers;

import com.opaltrace.platform.subscriptions.domain.model.aggregates.Subscription;
import com.opaltrace.platform.subscriptions.domain.model.entities.BillingRecord;
import com.opaltrace.platform.subscriptions.infrastructure.persistence.jpa.entities.BillingRecordPersistenceEntity;
import com.opaltrace.platform.subscriptions.infrastructure.persistence.jpa.entities.SubscriptionPersistenceEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class SubscriptionPersistenceAssembler {

    private SubscriptionPersistenceAssembler() {}

    public static Subscription toDomain(SubscriptionPersistenceEntity entity) {
        var subscription = new Subscription();
        subscription.setId(entity.getId());
        List<BillingRecord> billingRecords = entity.getBillingRecords() != null
                ? entity.getBillingRecords().stream().map(SubscriptionPersistenceAssembler::toBillingDomain).collect(Collectors.toList())
                : new ArrayList<>();
        subscription.reconstitute(
                entity.getUserId(),
                entity.getPlanTier(),
                entity.getStatus(),
                entity.getBillingCycle(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getNextBillingDate(),
                entity.isPendingDowngrade(),
                entity.getTargetDowngradeTier(),
                entity.getDowngradeEffectiveDate(),
                entity.getCancelEffectiveDate(),
                billingRecords
        );
        return subscription;
    }

    public static SubscriptionPersistenceEntity toPersistence(Subscription subscription) {
        var entity = new SubscriptionPersistenceEntity();
        if (subscription.getId() != null) {
            entity.setId(subscription.getId());
        }
        entity.setUserId(subscription.getUserId());
        entity.setPlanTier(subscription.getPlanTier());
        entity.setStatus(subscription.getStatus());
        entity.setBillingCycle(subscription.getBillingCycle());
        entity.setStartDate(subscription.getStartDate());
        entity.setEndDate(subscription.getEndDate());
        entity.setNextBillingDate(subscription.getNextBillingDate());
        entity.setPendingDowngrade(subscription.isPendingDowngrade());
        entity.setTargetDowngradeTier(subscription.getTargetDowngradeTier());
        entity.setDowngradeEffectiveDate(subscription.getDowngradeEffectiveDate());
        entity.setCancelEffectiveDate(subscription.getCancelEffectiveDate());

        entity.getBillingRecords().clear();
        if (subscription.getBillingRecords() != null) {
            subscription.getBillingRecords().forEach(br -> {
                var brEntity = toBillingPersistence(br, entity);
                entity.getBillingRecords().add(brEntity);
            });
        }
        return entity;
    }

    private static BillingRecord toBillingDomain(BillingRecordPersistenceEntity entity) {
        var record = new BillingRecord();
        record.setId(entity.getId());
        record.reconstitute(
                entity.getSubscriptionId(),
                entity.getInvoiceNumber(),
                entity.getAmount(),
                entity.getPlanTier(),
                entity.getPaymentStatus(),
                entity.getPaymentMethod(),
                entity.getTransactionDate()
        );
        return record;
    }

    private static BillingRecordPersistenceEntity toBillingPersistence(BillingRecord record,
                                                                        SubscriptionPersistenceEntity subscriptionEntity) {
        var entity = new BillingRecordPersistenceEntity();
        if (record.getId() != null) {
            entity.setId(record.getId());
        }
        entity.setSubscription(subscriptionEntity);
        entity.setSubscriptionId(record.getSubscriptionId());
        entity.setInvoiceNumber(record.getInvoiceNumber());
        entity.setAmount(record.getAmount());
        entity.setPlanTier(record.getPlanTier());
        entity.setPaymentStatus(record.getPaymentStatus());
        entity.setPaymentMethod(record.getPaymentMethod());
        entity.setTransactionDate(record.getTransactionDate());
        return entity;
    }
}
