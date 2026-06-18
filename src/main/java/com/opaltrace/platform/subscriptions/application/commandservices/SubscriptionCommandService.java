package com.opaltrace.platform.subscriptions.application.commandservices;

import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import com.opaltrace.platform.subscriptions.domain.model.commands.*;

public interface SubscriptionCommandService {
    Result<Long, ApplicationError> handle(ActivateSubscriptionCommand command);
    Result<Long, ApplicationError> handle(UpgradePlanCommand command);
    Result<Long, ApplicationError> handle(DowngradePlanCommand command);
    Result<Long, ApplicationError> handle(CancelSubscriptionCommand command);
}
