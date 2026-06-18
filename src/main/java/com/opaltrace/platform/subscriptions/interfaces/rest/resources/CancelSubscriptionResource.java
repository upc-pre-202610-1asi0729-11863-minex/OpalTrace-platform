package com.opaltrace.platform.subscriptions.interfaces.rest.resources;

public record CancelSubscriptionResource(
        Long userId,
        String cancellationReason
) {}
