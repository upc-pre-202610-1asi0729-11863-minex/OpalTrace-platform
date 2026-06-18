package com.opaltrace.platform.mineralextraction.application.internal.queryservices;

import com.opaltrace.platform.mineralextraction.application.queryservices.MineralBatchQueryService;
import com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch;
import com.opaltrace.platform.mineralextraction.domain.model.entities.AnomalyReport;
import com.opaltrace.platform.mineralextraction.domain.model.queries.*;
import com.opaltrace.platform.mineralextraction.domain.repositories.MineralBatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MineralBatchQueryServiceImpl implements MineralBatchQueryService {

    private final MineralBatchRepository mineralBatchRepository;

    public MineralBatchQueryServiceImpl(MineralBatchRepository mineralBatchRepository) {
        this.mineralBatchRepository = mineralBatchRepository;
    }

    @Override
    public Optional<MineralBatch> handle(GetBatchByIdQuery query) {
        return mineralBatchRepository.findById(query.batchPk());
    }

    @Override
    public Optional<MineralBatch> handle(GetBatchByBatchIdQuery query) {
        return mineralBatchRepository.findByBatchId(query.batchId());
    }

    @Override
    public List<MineralBatch> handle(GetAllBatchesQuery query) {
        return mineralBatchRepository.findAll();
    }

    @Override
    public List<MineralBatch> handle(GetBatchesByCompanyQuery query) {
        return mineralBatchRepository.findByMiningCompanyId(query.miningCompanyId());
    }

    @Override
    public List<AnomalyReport> handle(GetAnomalyAlertsByBatchQuery query) {
        return mineralBatchRepository.findById(query.batchPk())
                .map(MineralBatch::getAnomalyReports)
                .orElse(List.of());
    }
}
