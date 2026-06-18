package com.opaltrace.platform.jewelryinventory.interfaces.rest;

import com.opaltrace.platform.jewelryinventory.application.commandservices.JewelryInventoryCommandService;
import com.opaltrace.platform.jewelryinventory.application.queryservices.JewelryInventoryQueryService;
import com.opaltrace.platform.jewelryinventory.domain.model.commands.CertifyProductCommand;
import com.opaltrace.platform.jewelryinventory.domain.model.commands.GenerateCertificateCommand;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetProductByCertificateNumberQuery;
import com.opaltrace.platform.jewelryinventory.domain.model.queries.GetProductByIdQuery;
import com.opaltrace.platform.jewelryinventory.interfaces.rest.transform.JewelryProductResourceFromEntityAssembler;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import com.opaltrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/jewelry-inventory", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Product Certification", description = "Product certification and digital certificate generation")
public class ProductCertificationController {

    private final JewelryInventoryCommandService commandService;
    private final JewelryInventoryQueryService queryService;

    public ProductCertificationController(JewelryInventoryCommandService commandService,
                                           JewelryInventoryQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping("/{productId}/certify")
    @Operation(summary = "Certify product (US14)", description = "Certify a jewelry product. Validates all material has certified source and no active anomalies. Generates CERT-YYYY-NNNN number.")
    public ResponseEntity<?> certifyProduct(@PathVariable Long productId) {
        var command = new CertifyProductCommand(productId, null);
        var result = commandService.handle(command)
                .flatMap(id -> queryService.handle(new GetProductByIdQuery(id))
                        .<Result<com.opaltrace.platform.jewelryinventory.domain.model.aggregates.JewelryProduct, ApplicationError>>
                                map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("JewelryProduct", id.toString()))));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, JewelryProductResourceFromEntityAssembler::toResource, HttpStatus.OK);
    }

    @PostMapping("/{productId}/certificate")
    @Operation(summary = "Generate certificate (US15)", description = "Generate and download certificate data for a CERTIFIED product. Returns full traceability certificate.")
    public ResponseEntity<?> generateCertificate(@PathVariable Long productId) {
        var command = new GenerateCertificateCommand(productId);
        var result = commandService.handle(command)
                .flatMap(id -> queryService.handle(new GetProductByIdQuery(id))
                        .<Result<com.opaltrace.platform.jewelryinventory.domain.model.aggregates.JewelryProduct, ApplicationError>>
                                map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("JewelryProduct", id.toString()))));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, JewelryProductResourceFromEntityAssembler::toCertificateResource, HttpStatus.OK);
    }

    @GetMapping("/certificate/{certificateNumber}")
    @Operation(summary = "Get product by certificate number")
    public ResponseEntity<?> getProductByCertificateNumber(@PathVariable String certificateNumber) {
        var product = queryService.handle(new GetProductByCertificateNumberQuery(certificateNumber));
        if (product.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(JewelryProductResourceFromEntityAssembler.toResource(product.get()));
    }
}
