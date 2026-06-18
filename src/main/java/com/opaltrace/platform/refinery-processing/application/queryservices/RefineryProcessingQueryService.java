package com.opaltrace.platform.refineryprocessing.application.queryservices;

import com.opaltrace.platform.refineryprocessing.domain.model.aggregates.RefineryReceipt;
import com.opaltrace.platform.refineryprocessing.domain.model.entities.SubLotRecord;
import com.opaltrace.platform.refineryprocessing.domain.model.queries.GetAllRefineryReceiptsQuery;
import com.opaltrace.platform.refineryprocessing.domain.model.queries.GetRefineryReceiptByBatchPkQuery;
import com.opaltrace.platform.refineryprocessing.domain.model.queries.GetSubLotsByParentBatchPkQuery;

import java.util.List;
import java.util.Optional;

public interface RefineryProcessingQueryService {
    Optional<RefineryReceipt> handle(GetRefineryReceiptByBatchPkQuery query);
    List<SubLotRecord> handle(GetSubLotsByParentBatchPkQuery query);
    List<RefineryReceipt> handle(GetAllRefineryReceiptsQuery query);
}
