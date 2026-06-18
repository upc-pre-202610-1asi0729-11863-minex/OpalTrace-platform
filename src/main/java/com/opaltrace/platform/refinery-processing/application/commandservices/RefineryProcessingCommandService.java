package com.opaltrace.platform.refineryprocessing.application.commandservices;

import com.opaltrace.platform.refineryprocessing.domain.model.commands.CompleteProcessingCommand;
import com.opaltrace.platform.refineryprocessing.domain.model.commands.ReceiveBatchAtRefineryCommand;
import com.opaltrace.platform.refineryprocessing.domain.model.commands.SplitBatchCommand;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;

import java.util.List;

public interface RefineryProcessingCommandService {
    Result<Long, ApplicationError> handle(ReceiveBatchAtRefineryCommand command);
    Result<List<Long>, ApplicationError> handle(SplitBatchCommand command);
    Result<Long, ApplicationError> handle(CompleteProcessingCommand command);
}
