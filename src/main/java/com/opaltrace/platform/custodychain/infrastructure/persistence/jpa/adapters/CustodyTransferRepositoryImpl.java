package com.opaltrace.platform.custodychain.infrastructure.persistence.jpa.adapters;

import com.opaltrace.platform.custodychain.domain.model.aggregates.CustodyTransfer;
import com.opaltrace.platform.custodychain.domain.repositories.CustodyTransferRepository;
import com.opaltrace.platform.custodychain.infrastructure.persistence.jpa.assemblers.CustodyTransferPersistenceAssembler;
import com.opaltrace.platform.custodychain.infrastructure.persistence.jpa.repositories.CustodyTransferPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustodyTransferRepositoryImpl implements CustodyTransferRepository {

    private final CustodyTransferPersistenceRepository persistenceRepository;

    public CustodyTransferRepositoryImpl(CustodyTransferPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public CustodyTransfer save(CustodyTransfer transfer) {
        var entity = CustodyTransferPersistenceAssembler.toPersistenceFromDomain(transfer);
        var saved = persistenceRepository.save(entity);
        return CustodyTransferPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<CustodyTransfer> findByBatchPk(Long batchPk) {
        return persistenceRepository.findByBatchPk(batchPk)
                .map(CustodyTransferPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<CustodyTransfer> findAll() {
        return persistenceRepository.findAll().stream()
                .map(CustodyTransferPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<CustodyTransfer> findByCustodyHolderUserId(Long userId) {
        return persistenceRepository.findByCustodyHolderUserId(userId).stream()
                .map(CustodyTransferPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
