package com.opaltrace.platform.iam.domain.model.commands;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordCommand(
        Long userId,
        @NotBlank String currentPassword,
        @NotBlank String newPassword
) {}
