package com.opaltrace.platform.iam.domain.model.commands;

import jakarta.validation.constraints.NotBlank;

public record RegisterConsumerUserCommand(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String fullName,
        @NotBlank String cardNumber
) {}
