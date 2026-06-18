package com.opaltrace.platform.jewelryinventory.infrastructure.persistence.jpa.repositories;

import com.opaltrace.platform.jewelryinventory.domain.model.valueobjects.InventoryCategory;
import com.opaltrace.platform.jewelryinventory.infrastructure.persistence.jpa.entities.JewelryProductPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JewelryProductPersistenceRepository extends JpaRepository<JewelryProductPersistenceEntity, Long> {

    List<JewelryProductPersistenceEntity> findByJewelryIdAndCategory(Long jewelryId, InventoryCategory category);

    Optional<JewelryProductPersistenceEntity> findByCertificateNumber(String certificateNumber);

    @Query("SELECT COUNT(p) FROM JewelryProductPersistenceEntity p WHERE YEAR(p.createdAt) = :year")
    long countByYear(@Param("year") int year);
}
