package com.opaltrace.platform.iam.domain.model.commands;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileCommand(
        Long userId,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String email,
        String gender
) {}
