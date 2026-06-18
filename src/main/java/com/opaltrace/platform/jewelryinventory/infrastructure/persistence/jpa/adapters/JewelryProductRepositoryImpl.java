package com.opaltrace.platform.jewelryinventory.infrastructure.persistence.jpa.adapters;

import com.opaltrace.platform.jewelryinventory.domain.model.aggregates.JewelryProduct;
import com.opaltrace.platform.jewelryinventory.domain.model.valueobjects.InventoryCategory;
import com.opaltrace.platform.jewelryinventory.domain.repositories.JewelryProductRepository;
import com.opaltrace.platform.jewelryinventory.infrastructure.persistence.jpa.assemblers.JewelryProductPersistenceAssembler;
import com.opaltrace.platform.jewelryinventory.infrastructure.persistence.jpa.repositories.JewelryProductPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JewelryProductRepositoryImpl implements JewelryProductRepository {

    private final JewelryProductPersistenceRepository persistenceRepository;

    public JewelryProductRepositoryImpl(JewelryProductPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public JewelryProduct save(JewelryProduct product) {
        var entity = JewelryProductPersistenceAssembler.toPersistence(product);
        var saved = persistenceRepository.save(entity);
        return JewelryProductPersistenceAssembler.toDomain(saved);
    }

    @Override
    public Optional<JewelryProduct> findById(Long id) {
        return persistenceRepository.findById(id)
                .map(JewelryProductPersistenceAssembler::toDomain);
    }

    @Override
    public List<JewelryProduct> findAll() {
        return persistenceRepository.findAll().stream()
                .map(JewelryProductPersistenceAssembler::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<JewelryProduct> findByJewelryIdAndCategory(Long jewelryId, InventoryCategory category) {
        return persistenceRepository.findByJewelryIdAndCategory(jewelryId, category).stream()
                .map(JewelryProductPersistenceAssembler::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<JewelryProduct> findByCertificateNumber(String certificateNumber) {
        return persistenceRepository.findByCertificateNumber(certificateNumber)
                .map(JewelryProductPersistenceAssembler::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return persistenceRepository.existsById(id);
    }

    @Override
    public long countByYear(int year) {
        return persistenceRepository.countByYear(year);
    }
}
