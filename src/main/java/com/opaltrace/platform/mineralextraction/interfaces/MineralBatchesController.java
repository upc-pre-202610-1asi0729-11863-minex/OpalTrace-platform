package com.opaltrace.platform.mineralextraction.interfaces.rest;

import com.opaltrace.platform.mineralextraction.application.commandservices.MineralBatchCommandService;
import com.opaltrace.platform.mineralextraction.application.queryservices.MineralBatchQueryService;
import com.opaltrace.platform.mineralextraction.domain.model.commands.GenerateBatchQrCommand;
import com.opaltrace.platform.mineralextraction.domain.model.commands.SyncOfflineBatchCommand;
import com.opaltrace.platform.mineralextraction.domain.model.queries.GetAllBatchesQuery;
import com.opaltrace.platform.mineralextraction.domain.model.queries.GetBatchByBatchIdQuery;
import com.opaltrace.platform.mineralextraction.domain.model.queries.GetBatchByIdQuery;
import com.opaltrace.platform.mineralextraction.domain.model.queries.GetBatchesByCompanyQuery;
import com.opaltrace.platform.mineralextraction.interfaces.rest.resources.*;
import com.opaltrace.platform.mineralextraction.interfaces.rest.transform.*;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import com.opaltrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/mineral-batches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Mineral Batches", description = "Mineral extraction and batch traceability endpoints")
public class MineralBatchesController {

    private final MineralBatchCommandService mineralBatchCommandService;
    private final MineralBatchQueryService mineralBatchQueryService;

    public MineralBatchesController(MineralBatchCommandService mineralBatchCommandService,
                                    MineralBatchQueryService mineralBatchQueryService) {
        this.mineralBatchCommandService = mineralBatchCommandService;
        this.mineralBatchQueryService = mineralBatchQueryService;
    }

    @PostMapping
    @Operation(summary = "Register mineral batch (US01)", description = "Registers a new mineral extraction batch with GPS validation against authorized zones. Generates OT-YYYY-NNNN ID and MineralExtracted blockchain event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Batch registered successfully",
                    content = @Content(schema = @Schema(implementation = MineralBatchResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid weight or mineral type"),
            @ApiResponse(responseCode = "422", description = "GPS coordinates outside authorized zone")
    })
    public ResponseEntity<?> registerBatch(@Valid @RequestBody RegisterMineralBatchResource resource) {
        var command = RegisterMineralBatchCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = mineralBatchCommandService.handle(command)
                .flatMap(batchPk -> mineralBatchQueryService.handle(new GetBatchByIdQuery(batchPk))
                        .<Result<com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch, ApplicationError>>
                                map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("MineralBatch", batchPk.toString()))));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, MineralBatchResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping("/{batchPk}")
    @Operation(summary = "Get batch by primary key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Batch found",
                    content = @Content(schema = @Schema(implementation = MineralBatchResource.class))),
            @ApiResponse(responseCode = "404", description = "Batch not found")
    })
    public ResponseEntity<?> getBatchById(
            @PathVariable @Parameter(description = "Internal batch PK", required = true) Long batchPk) {
        var batch = mineralBatchQueryService.handle(new GetBatchByIdQuery(batchPk));
        if (batch.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(MineralBatchResourceFromEntityAssembler.toResourceFromEntity(batch.get()));
    }

    @GetMapping("/code/{batchId}")
    @Operation(summary = "Get batch by OT-YYYY-NNNN code (US07)", description = "Retrieves a batch by its human-readable OT code, used during custody transfers.")
    public ResponseEntity<?> getBatchByCode(
            @PathVariable @Parameter(description = "Batch code e.g. OT-2026-0001", required = true) String batchId) {
        var batch = mineralBatchQueryService.handle(new GetBatchByBatchIdQuery(batchId));
        if (batch.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(MineralBatchResourceFromEntityAssembler.toResourceFromEntity(batch.get()));
    }

    @GetMapping
    @Operation(summary = "Get all batches")
    public ResponseEntity<List<MineralBatchResource>> getAllBatches() {
        var batches = mineralBatchQueryService.handle(new GetAllBatchesQuery()).stream()
                .map(MineralBatchResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(batches);
    }

    @GetMapping("/company/{miningCompanyId}")
    @Operation(summary = "Get batches by mining company")
    public ResponseEntity<List<MineralBatchResource>> getBatchesByCompany(
            @PathVariable Long miningCompanyId) {
        var batches = mineralBatchQueryService.handle(new GetBatchesByCompanyQuery(miningCompanyId)).stream()
                .map(MineralBatchResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(batches);
    }

    @PostMapping("/{batchPk}/qr")
    @Operation(summary = "Generate QR for batch (US06)", description = "Generates a QR code with digital signature for a valid, unblocked batch.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "QR generated",
                    content = @Content(schema = @Schema(implementation = MineralBatchResource.class))),
            @ApiResponse(responseCode = "404", description = "Batch not found"),
            @ApiResponse(responseCode = "422", description = "Batch is blocked or traceability is incomplete")
    })
    public ResponseEntity<?> generateQr(@PathVariable Long batchPk) {
        var result = mineralBatchCommandService.handle(new GenerateBatchQrCommand(batchPk));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, MineralBatchResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PostMapping("/sync")
    @Operation(summary = "Sync offline batch (US03)", description = "Syncs a batch recorded offline. Duplicates identified by offlineBatchId are silently discarded.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Batch synced or duplicate discarded"),
            @ApiResponse(responseCode = "422", description = "GPS outside authorized zone")
    })
    public ResponseEntity<?> syncOfflineBatch(@Valid @RequestBody SyncOfflineBatchResource resource) {
        var command = new SyncOfflineBatchCommand(
                resource.offlineBatchId(),
                resource.mineralType(),
                resource.weightKg(),
                resource.latitude(),
                resource.longitude(),
                resource.supervisorId(),
                resource.miningCompanyId(),
                resource.originalTimestamp()
        );
        var result = mineralBatchCommandService.handle(command)
                .flatMap(batchPk -> {
                    if (batchPk == -1L)
                        return Result.success(null);
                    return mineralBatchQueryService.handle(new GetBatchByIdQuery(batchPk))
                            .<Result<com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch, ApplicationError>>
                                    map(Result::success)
                            .orElseGet(() -> Result.failure(ApplicationError.notFound("MineralBatch", batchPk.toString())));
                });
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                b -> b != null ? MineralBatchResourceFromEntityAssembler.toResourceFromEntity(b) : "Duplicate discarded",
                HttpStatus.CREATED);
    }
}
