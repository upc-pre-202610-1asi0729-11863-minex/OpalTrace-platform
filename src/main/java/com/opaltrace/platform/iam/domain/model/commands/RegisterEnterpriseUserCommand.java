package com.opaltrace.platform.iam.domain.model.commands;

import com.opaltrace.platform.iam.domain.model.valueobjects.UserSegment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterEnterpriseUserCommand(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String companyName,
        @NotBlank String ruc,
        @NotNull UserSegment segment,
        @NotBlank String cardNumber
) {}
