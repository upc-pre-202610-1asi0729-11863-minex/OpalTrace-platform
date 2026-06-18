package com.opaltrace.platform.mineralextraction.interfaces.rest.transform;

import com.opaltrace.platform.mineralextraction.domain.model.commands.RegisterMineralBatchCommand;
import com.opaltrace.platform.mineralextraction.interfaces.rest.resources.RegisterMineralBatchResource;

public final class RegisterMineralBatchCommandFromResourceAssembler {

    private RegisterMineralBatchCommandFromResourceAssembler() {}

    public static RegisterMineralBatchCommand toCommandFromResource(RegisterMineralBatchResource resource) {
        return new RegisterMineralBatchCommand(
                resource.mineralType(),
                resource.weightKg(),
                resource.latitude(),
                resource.longitude(),
                resource.supervisorId(),
                resource.miningCompanyId()
        );
    }
}
