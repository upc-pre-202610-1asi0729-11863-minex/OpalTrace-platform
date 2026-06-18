package com.opaltrace.platform.jewelryinventory.application.commandservices;

import com.opaltrace.platform.jewelryinventory.domain.model.commands.CertifyProductCommand;
import com.opaltrace.platform.jewelryinventory.domain.model.commands.GenerateCertificateCommand;
import com.opaltrace.platform.jewelryinventory.domain.model.commands.ReceiveCertifiedMaterialCommand;
import com.opaltrace.platform.jewelryinventory.domain.model.commands.RegisterExternalMaterialCommand;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;

public interface JewelryInventoryCommandService {
    Result<Long, ApplicationError> handle(ReceiveCertifiedMaterialCommand command);
    Result<Long, ApplicationError> handle(RegisterExternalMaterialCommand command);
    Result<Long, ApplicationError> handle(CertifyProductCommand command);
    Result<Long, ApplicationError> handle(GenerateCertificateCommand command);
}
