package com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.adapters;

import com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.BatchStatus;
import com.opaltrace.platform.mineralextraction.domain.repositories.MineralBatchRepository;
import com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.assemblers.MineralBatchPersistenceAssembler;
import com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.repositories.MineralBatchPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MineralBatchRepositoryImpl implements MineralBatchRepository {

    private final MineralBatchPersistenceRepository mineralBatchPersistenceRepository;

    public MineralBatchRepositoryImpl(MineralBatchPersistenceRepository mineralBatchPersistenceRepository) {
        this.mineralBatchPersistenceRepository = mineralBatchPersistenceRepository;
    }

    @Override
    public MineralBatch save(MineralBatch batch) {
        var saved = mineralBatchPersistenceRepository.save(
                MineralBatchPersistenceAssembler.toPersistenceFromDomain(batch));
        return MineralBatchPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<MineralBatch> findById(Long id) {
        return mineralBatchPersistenceRepository.findById(id)
                .map(MineralBatchPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<MineralBatch> findByBatchId(String batchId) {
        return mineralBatchPersistenceRepository.findByBatchId(batchId)
                .map(MineralBatchPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<MineralBatch> findAll() {
        return mineralBatchPersistenceRepository.findAll().stream()
                .map(MineralBatchPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<MineralBatch> findByMiningCompanyId(Long miningCompanyId) {
        return mineralBatchPersistenceRepository.findByMiningCompanyId(miningCompanyId).stream()
                .map(MineralBatchPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<MineralBatch> findByStatus(BatchStatus status) {
        return mineralBatchPersistenceRepository.findByStatus(status).stream()
                .map(MineralBatchPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsByBatchId(String batchId) {
        return mineralBatchPersistenceRepository.existsByBatchId(batchId);
    }

    @Override
    public int countByYear(int year) {
        return mineralBatchPersistenceRepository.countByYear(year);
    }
}
