package com.opaltrace.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record SignInResource(
        @NotBlank @jakarta.validation.constraints.Email String email,
        @NotBlank String password
) {}
