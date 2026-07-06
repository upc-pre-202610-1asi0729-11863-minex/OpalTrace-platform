package com.opaltrace.platform.iam.domain.model.commands;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordCommand(
        @NotBlank @Email String email
) {}
