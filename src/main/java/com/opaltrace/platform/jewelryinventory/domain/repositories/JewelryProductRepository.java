package com.opaltrace.platform.jewelryinventory.domain.repositories;

import com.opaltrace.platform.jewelryinventory.domain.model.aggregates.JewelryProduct;
import com.opaltrace.platform.jewelryinventory.domain.model.valueobjects.InventoryCategory;

import java.util.List;
import java.util.Optional;

public interface JewelryProductRepository {
    JewelryProduct save(JewelryProduct product);
    Optional<JewelryProduct> findById(Long id);
    List<JewelryProduct> findAll();
    List<JewelryProduct> findByJewelryIdAndCategory(Long jewelryId, InventoryCategory category);
    Optional<JewelryProduct> findByCertificateNumber(String certificateNumber);
    boolean existsById(Long id);
    long countByYear(int year);
}
