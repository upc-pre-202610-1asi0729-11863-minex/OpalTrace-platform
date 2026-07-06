package com.opaltrace.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateProfileResource(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        String gender
) {}
