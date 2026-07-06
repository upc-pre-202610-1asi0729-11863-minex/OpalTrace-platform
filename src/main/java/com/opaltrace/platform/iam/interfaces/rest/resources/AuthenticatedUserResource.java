package com.opaltrace.platform.iam.interfaces.rest.resources;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.iam.domain.model.valueobjects.UserRole;
import com.opaltrace.platform.iam.domain.model.valueobjects.UserSegment;

public record AuthenticatedUserResource(
        Long id,
        String email,
        String fullName,
        String companyName,
        UserSegment segment,
        UserRole role,
        PlanTier planTier,
        String token,
        String gender
) {}
