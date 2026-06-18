package com.opaltrace.platform.analytics.application.commandservices;

import com.opaltrace.platform.analytics.domain.model.commands.GenerateComparativeAnalysisCommand;
import com.opaltrace.platform.analytics.domain.model.commands.GenerateEsgReportCommand;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;

public interface AnalyticsCommandService {
    Result<Long, ApplicationError> handle(GenerateEsgReportCommand command);
    Result<Long, ApplicationError> handle(GenerateComparativeAnalysisCommand command);
}
