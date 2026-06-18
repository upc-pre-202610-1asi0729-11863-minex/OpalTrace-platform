package com.opaltrace.platform.jewelryinventory.application.internal.queryservices;

import com.opaltrace.platform.jewelryinventory.application.queryservices.JewelryInventoryQueryService;
import com.opaltrace.platform.jewelryinventory.domain.model.aggregates.JewelryProduct;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetAllProductsQuery;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetCertifiedInventoryByJewelryQuery;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetExternalInventoryByJewelryQuery;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetProductByCertificateNumberQuery;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetProductByIdQuery;
import com.opaltrace.platform.jewelryinventory.domain.model.valueobjects.InventoryCategory;
import com.opaltrace.platform.jewelryinventory.domain.repositories.JewelryProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JewelryInventoryQueryServiceImpl implements JewelryInventoryQueryService {

    private final JewelryProductRepository jewelryProductRepository;

    public JewelryInventoryQueryServiceImpl(JewelryProductRepository jewelryProductRepository) {
        this.jewelryProductRepository = jewelryProductRepository;
    }

    @Override
    public Optional<JewelryProduct> handle(GetProductByIdQuery query) {
        return jewelryProductRepository.findById(query.productId());
    }

    @Override
    public List<JewelryProduct> handle(GetCertifiedInventoryByJewelryQuery query) {
        return jewelryProductRepository.findByJewelryIdAndCategory(query.jewelryId(), InventoryCategory.CERTIFIED_STOCK);
    }

    @Override
    public List<JewelryProduct> handle(GetExternalInventoryByJewelryQuery query) {
        return jewelryProductRepository.findByJewelryIdAndCategory(query.jewelryId(), InventoryCategory.EXTERNAL_STOCK);
    }

    @Override
    public List<JewelryProduct> handle(GetAllProductsQuery query) {
        return jewelryProductRepository.findAll();
    }

    @Override
    public Optional<JewelryProduct> handle(GetProductByCertificateNumberQuery query) {
        return jewelryProductRepository.findByCertificateNumber(query.certificateNumber());
    }
}
