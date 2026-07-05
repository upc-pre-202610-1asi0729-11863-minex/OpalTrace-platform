package com.opaltrace.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordResource(
        @NotNull Long userId,
        @NotBlank String currentPassword,
        @NotBlank String newPassword
) {}
