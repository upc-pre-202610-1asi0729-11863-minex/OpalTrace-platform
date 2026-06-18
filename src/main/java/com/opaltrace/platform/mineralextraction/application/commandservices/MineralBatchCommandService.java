package com.opaltrace.platform.mineralextraction.application.commandservices;

import com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch;
import com.opaltrace.platform.mineralextraction.domain.model.commands.*;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;

public interface MineralBatchCommandService {
    Result<Long, ApplicationError> handle(RegisterMineralBatchCommand command);
    Result<Long, ApplicationError> handle(ReportAnomalyCommand command);
    Result<MineralBatch, ApplicationError> handle(GenerateBatchQrCommand command);
    Result<Long, ApplicationError> handle(SyncOfflineBatchCommand command);
}
