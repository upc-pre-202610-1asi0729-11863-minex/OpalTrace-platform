package com.opaltrace.platform.iam.domain.model.commands;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import jakarta.validation.constraints.NotBlank;

public record RegisterConsumerUserCommand(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String fullName,
        @NotBlank String cardNumber,
        String gender,
        PlanTier planTier
) {}
