package com.opaltrace.platform.refineryprocessing.application.internal.queryservices;

import com.opaltrace.platform.refineryprocessing.application.queryservices.RefineryProcessingQueryService;
import com.opaltrace.platform.refineryprocessing.domain.model.aggregates.RefineryReceipt;
import com.opaltrace.platform.refineryprocessing.domain.model.entities.SubLotRecord;
import com.opaltrace.platform.refineryprocessing.domain.model.queries.GetAllRefineryReceiptsQuery;
import com.opaltrace.platform.refineryprocessing.domain.model.queries.GetRefineryReceiptByBatchPkQuery;
import com.opaltrace.platform.refineryprocessing.domain.model.queries.GetSubLotsByParentBatchPkQuery;
import com.opaltrace.platform.refineryprocessing.domain.repositories.RefineryReceiptRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RefineryProcessingQueryServiceImpl implements RefineryProcessingQueryService {

    private final RefineryReceiptRepository refineryReceiptRepository;

    public RefineryProcessingQueryServiceImpl(RefineryReceiptRepository refineryReceiptRepository) {
        this.refineryReceiptRepository = refineryReceiptRepository;
    }

    @Override
    public Optional<RefineryReceipt> handle(GetRefineryReceiptByBatchPkQuery query) {
        return refineryReceiptRepository.findByBatchPk(query.batchPk());
    }

    @Override
    public List<SubLotRecord> handle(GetSubLotsByParentBatchPkQuery query) {
        return refineryReceiptRepository.findByBatchPk(query.parentBatchPk())
                .map(RefineryReceipt::getSubLots)
                .orElse(Collections.emptyList());
    }

    @Override
    public List<RefineryReceipt> handle(GetAllRefineryReceiptsQuery query) {
        return refineryReceiptRepository.findAll();
    }
}
