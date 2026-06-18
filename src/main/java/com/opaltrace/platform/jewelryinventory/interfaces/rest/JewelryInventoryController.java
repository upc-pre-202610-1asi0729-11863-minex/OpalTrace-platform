package com.opaltrace.platform.jewelryinventory.interfaces.rest;

import com.opaltrace.platform.jewelryinventory.application.commandservices.JewelryInventoryCommandService;
import com.opaltrace.platform.jewelryinventory.application.queryservices.JewelryInventoryQueryService;
import com.opaltrace.platform.jewelryinventory.domain.model.commands.ReceiveCertifiedMaterialCommand;
import com.opaltrace.platform.jewelryinventory.domain.model.commands.RegisterExternalMaterialCommand;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetAllProductsQuery;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetCertifiedInventoryByJewelryQuery;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetExternalInventoryByJewelryQuery;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetProductByIdQuery;
import com.opaltrace.platform.jewelryinventory.interfaces.rest.resources.JewelryProductResource;
import com.opaltrace.platform.jewelryinventory.interfaces.rest.resources.ReceiveCertifiedMaterialResource;
import com.opaltrace.platform.jewelryinventory.interfaces.rest.resources.RegisterExternalMaterialResource;
import com.opaltrace.platform.jewelryinventory.interfaces.rest.transform.JewelryProductResourceFromEntityAssembler;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import com.opaltrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/jewelry-inventory", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Jewelry Inventory", description = "Certified and external material inventory management")
public class JewelryInventoryController {

    private final JewelryInventoryCommandService commandService;
    private final JewelryInventoryQueryService queryService;

    public JewelryInventoryController(JewelryInventoryCommandService commandService,
                                       JewelryInventoryQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping("/receive-certified")
    @Operation(summary = "Receive certified material (US12)", description = "Receive certified mineral material by scanning QR/batchId. Validates batch traceability and blocked status.")
    public ResponseEntity<?> receiveCertifiedMaterial(@Valid @RequestBody ReceiveCertifiedMaterialResource resource) {
        var command = new ReceiveCertifiedMaterialCommand(
                resource.batchPk(),
                resource.batchId(),
                resource.jewelryId(),
                resource.name(),
                resource.weightGrams()
        );
        var result = commandService.handle(command)
                .flatMap(productId -> queryService.handle(new GetProductByIdQuery(productId))
                        .<Result<com.opaltrace.platform.jewelryinventory.domain.model.aggregates.JewelryProduct, ApplicationError>>
                                map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("JewelryProduct", productId.toString()))));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, JewelryProductResourceFromEntityAssembler::toResource, HttpStatus.CREATED);
    }

    @PostMapping("/register-external")
    @Operation(summary = "Register external material (US13)", description = "Register material from external suppliers. Sets isCertifiedSource=false and canGenerateCertificate=false.")
    public ResponseEntity<?> registerExternalMaterial(@Valid @RequestBody RegisterExternalMaterialResource resource) {
        var command = new RegisterExternalMaterialCommand(
                resource.jewelryId(),
                resource.name(),
                resource.externalSupplierName(),
                resource.weightGrams(),
                resource.description()
        );
        var result = commandService.handle(command)
                .flatMap(productId -> queryService.handle(new GetProductByIdQuery(productId))
                        .<Result<com.opaltrace.platform.jewelryinventory.domain.model.aggregates.JewelryProduct, ApplicationError>>
                                map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("JewelryProduct", productId.toString()))));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, JewelryProductResourceFromEntityAssembler::toResource, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        var product = queryService.handle(new GetProductByIdQuery(productId));
        if (product.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(JewelryProductResourceFromEntityAssembler.toResource(product.get()));
    }

    @GetMapping("/certified")
    @Operation(summary = "Get certified inventory by jewelry (US12)")
    public ResponseEntity<List<JewelryProductResource>> getCertifiedInventory(@RequestParam Long jewelryId) {
        var products = queryService.handle(new GetCertifiedInventoryByJewelryQuery(jewelryId)).stream()
                .map(JewelryProductResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/external")
    @Operation(summary = "Get external inventory by jewelry (US13)")
    public ResponseEntity<List<JewelryProductResource>> getExternalInventory(@RequestParam Long jewelryId) {
        var products = queryService.handle(new GetExternalInventoryByJewelryQuery(jewelryId)).stream()
                .map(JewelryProductResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<List<JewelryProductResource>> getAllProducts() {
        var products = queryService.handle(new GetAllProductsQuery()).stream()
                .map(JewelryProductResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(products);
    }
}
