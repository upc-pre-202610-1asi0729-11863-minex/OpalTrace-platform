package com.opaltrace.platform.iam.interfaces.rest.resources;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.iam.domain.model.valueobjects.UserSegment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterEnterpriseUserResource(
        @NotBlank @jakarta.validation.constraints.Email String email,
        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters") String password,
        @NotBlank String companyName,
        @NotBlank @Pattern(regexp = "\\d{11}", message = "RUC must be exactly 11 digits") String ruc,
        @NotNull UserSegment segment,
        @NotBlank String cardNumber,
        @Pattern(regexp = "M|F", message = "Gender must be M or F") String gender,
        PlanTier planTier
) {}
