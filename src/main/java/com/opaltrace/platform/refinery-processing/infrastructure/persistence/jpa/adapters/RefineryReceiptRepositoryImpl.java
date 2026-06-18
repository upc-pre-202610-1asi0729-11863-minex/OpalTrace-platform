package com.opaltrace.platform.refineryprocessing.infrastructure.persistence.jpa.adapters;

import com.opaltrace.platform.refineryprocessing.domain.model.aggregates.RefineryReceipt;
import com.opaltrace.platform.refineryprocessing.domain.repositories.RefineryReceiptRepository;
import com.opaltrace.platform.refineryprocessing.infrastructure.persistence.jpa.assemblers.RefineryReceiptPersistenceAssembler;
import com.opaltrace.platform.refineryprocessing.infrastructure.persistence.jpa.repositories.RefineryReceiptPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RefineryReceiptRepositoryImpl implements RefineryReceiptRepository {

    private final RefineryReceiptPersistenceRepository persistenceRepository;

    public RefineryReceiptRepositoryImpl(RefineryReceiptPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public RefineryReceipt save(RefineryReceipt receipt) {
        var entity = RefineryReceiptPersistenceAssembler.toPersistenceFromDomain(receipt);
        var saved = persistenceRepository.save(entity);
        return RefineryReceiptPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<RefineryReceipt> findByBatchPk(Long batchPk) {
        return persistenceRepository.findByBatchPk(batchPk)
                .map(RefineryReceiptPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<RefineryReceipt> findAll() {
        return persistenceRepository.findAll().stream()
                .map(RefineryReceiptPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsByBatchPk(Long batchPk) {
        return persistenceRepository.existsByBatchPk(batchPk);
    }
}
