package com.opaltrace.platform.subscriptions.domain.model.commands;

import jakarta.validation.constraints.NotNull;

public record CancelSubscriptionCommand(
        @NotNull Long subscriptionId,
        Long userId,
        String cancellationReason
) {}
