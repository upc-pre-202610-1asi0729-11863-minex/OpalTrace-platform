package com.opaltrace.platform.consumerexperience.application.commandservices;

import com.opaltrace.platform.consumerexperience.domain.model.aggregates.ProductVerification;
import com.opaltrace.platform.consumerexperience.domain.model.commands.RecordTraceabilityViewCommand;
import com.opaltrace.platform.consumerexperience.domain.model.commands.VerifyProductAuthenticityCommand;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;

public interface ConsumerExperienceCommandService {
    Result<ProductVerification, ApplicationError> handle(VerifyProductAuthenticityCommand command);
    Result<Long, ApplicationError> handle(RecordTraceabilityViewCommand command);
}
