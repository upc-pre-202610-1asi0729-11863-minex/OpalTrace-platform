package com.opaltrace.platform.subscriptions.domain.exceptions;

public class SubscriptionAlreadyActiveException extends RuntimeException {
    public SubscriptionAlreadyActiveException(Long userId) {
        super("User already has an active subscription: userId=" + userId);
    }
}
