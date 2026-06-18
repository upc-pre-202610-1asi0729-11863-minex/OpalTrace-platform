package com.opaltrace.platform.jewelryinventory.application.queryservices;

import com.opaltrace.platform.jewelryinventory.domain.model.aggregates.JewelryProduct;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetAllProductsQuery;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetCertifiedInventoryByJewelryQuery;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetExternalInventoryByJewelryQuery;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetProductByCertificateNumberQuery;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetProductByIdQuery;

import java.util.List;
import java.util.Optional;

public interface JewelryInventoryQueryService {
    Optional<JewelryProduct> handle(GetProductByIdQuery query);
    List<JewelryProduct> handle(GetCertifiedInventoryByJewelryQuery query);
    List<JewelryProduct> handle(GetExternalInventoryByJewelryQuery query);
    List<JewelryProduct> handle(GetAllProductsQuery query);
    Optional<JewelryProduct> handle(GetProductByCertificateNumberQuery query);
}
