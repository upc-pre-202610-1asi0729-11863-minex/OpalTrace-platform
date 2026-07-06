package com.opaltrace.platform.iam.interfaces.rest.transform;

import com.opaltrace.platform.iam.domain.model.commands.RegisterEnterpriseUserCommand;
import com.opaltrace.platform.iam.interfaces.rest.resources.RegisterEnterpriseUserResource;

public final class RegisterEnterpriseUserCommandFromResourceAssembler {

    private RegisterEnterpriseUserCommandFromResourceAssembler() {}

    public static RegisterEnterpriseUserCommand toCommandFromResource(RegisterEnterpriseUserResource resource) {
        return new RegisterEnterpriseUserCommand(
                resource.email(),
                resource.password(),
                resource.companyName(),
                resource.ruc(),
                resource.segment(),
                resource.cardNumber(),
                resource.gender()
        );
    }
}
