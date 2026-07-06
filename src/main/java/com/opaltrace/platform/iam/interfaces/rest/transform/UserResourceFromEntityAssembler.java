package com.opaltrace.platform.iam.interfaces.rest.transform;

import com.opaltrace.platform.iam.domain.model.aggregates.User;
import com.opaltrace.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.opaltrace.platform.iam.interfaces.rest.resources.UserResource;

public final class UserResourceFromEntityAssembler {

    private UserResourceFromEntityAssembler() {}

    public static UserResource toResourceFromEntity(User user) {
        return new UserResource(
                user.getId(),
                user.getEmail().value(),
                user.getFullName(),
                user.getCompanyName(),
                user.getRuc() != null ? user.getRuc().value() : null,
                user.getSegment(),
                user.getRole(),
                user.getPlanTier(),
                user.isActive(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender()
        );
    }

    public static AuthenticatedUserResource toAuthenticatedResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(
                user.getId(),
                user.getEmail().value(),
                user.getFullName(),
                user.getCompanyName(),
                user.getSegment(),
                user.getRole(),
                user.getPlanTier(),
                token,
                user.getGender()
        );
    }
}
