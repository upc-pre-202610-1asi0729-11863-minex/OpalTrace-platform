package com.opaltrace.platform.iam.interfaces.rest.transform;

import com.opaltrace.platform.iam.domain.model.commands.RegisterConsumerUserCommand;
import com.opaltrace.platform.iam.interfaces.rest.resources.RegisterConsumerUserResource;

public final class RegisterConsumerUserCommandFromResourceAssembler {

    private RegisterConsumerUserCommandFromResourceAssembler() {}

    public static RegisterConsumerUserCommand toCommandFromResource(RegisterConsumerUserResource resource) {
        return new RegisterConsumerUserCommand(
                resource.email(),
                resource.password(),
                resource.fullName(),
                resource.cardNumber(),
                resource.gender()
        );
    }
}
