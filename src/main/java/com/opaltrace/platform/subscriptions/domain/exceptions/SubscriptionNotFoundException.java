package com.opaltrace.platform.subscriptions.domain.exceptions;

public class SubscriptionNotFoundException extends RuntimeException {
    public SubscriptionNotFoundException(Long subscriptionId) {
        super("Subscription not found with id: " + subscriptionId);
    }
}
