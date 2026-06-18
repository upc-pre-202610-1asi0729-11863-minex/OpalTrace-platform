package com.opaltrace.platform.mineralextraction.application.queryservices;

import com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch;
import com.opaltrace.platform.mineralextraction.domain.model.entities.AnomalyReport;
import com.opaltrace.platform.mineralextraction.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface MineralBatchQueryService {
    Optional<MineralBatch> handle(GetBatchByIdQuery query);
    Optional<MineralBatch> handle(GetBatchByBatchIdQuery query);
    List<MineralBatch> handle(GetAllBatchesQuery query);
    List<MineralBatch> handle(GetBatchesByCompanyQuery query);
    List<AnomalyReport> handle(GetAnomalyAlertsByBatchQuery query);
}
