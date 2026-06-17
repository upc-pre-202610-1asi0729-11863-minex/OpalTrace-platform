package com.opaltrace.platform.iam.application.commandservices;

import com.opaltrace.platform.iam.domain.model.aggregates.User;
import com.opaltrace.platform.iam.domain.model.commands.ChangePasswordCommand;
import com.opaltrace.platform.iam.domain.model.commands.RegisterConsumerUserCommand;
import com.opaltrace.platform.iam.domain.model.commands.RegisterEnterpriseUserCommand;
import com.opaltrace.platform.iam.domain.model.commands.SignInCommand;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;

public interface UserCommandService {
    Result<Long, ApplicationError> handle(RegisterEnterpriseUserCommand command);
    Result<Long, ApplicationError> handle(RegisterConsumerUserCommand command);
    Result<User, ApplicationError> handle(SignInCommand command);
    Result<Long, ApplicationError> handle(ChangePasswordCommand command);
}
