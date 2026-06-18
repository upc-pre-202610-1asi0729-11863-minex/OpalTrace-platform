package com.opaltrace.platform.custodychain.application.commandservices;

import com.opaltrace.platform.custodychain.domain.model.commands.AcceptCustodyCommand;
import com.opaltrace.platform.custodychain.domain.model.commands.UpdateLocationCommand;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;

public interface CustodyChainCommandService {
    Result<Long, ApplicationError> handle(AcceptCustodyCommand command);
    Result<Long, ApplicationError> handle(UpdateLocationCommand command);
}
