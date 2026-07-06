package com.opaltrace.platform.iam.domain.model.commands;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordCommand(
        @NotBlank String token,
        @NotBlank String newPassword
) {}
