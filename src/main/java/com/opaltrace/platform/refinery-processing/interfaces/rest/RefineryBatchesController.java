package com.opaltrace.platform.refineryprocessing.interfaces.rest;

import com.opaltrace.platform.refineryprocessing.application.commandservices.RefineryProcessingCommandService;
import com.opaltrace.platform.refineryprocessing.application.queryservices.RefineryProcessingQueryService;
import com.opaltrace.platform.refineryprocessing.domain.model.commands.CompleteProcessingCommand;
import com.opaltrace.platform.refineryprocessing.domain.model.commands.ReceiveBatchAtRefineryCommand;
import com.opaltrace.platform.refineryprocessing.domain.model.commands.SplitBatchCommand;
import com.opaltrace.platform.refineryprocessing.domain.model.queries.GetAllRefineryReceiptsQuery;
import com.opaltrace.platform.refineryprocessing.domain.model.queries.GetRefineryReceiptByBatchPkQuery;
import com.opaltrace.platform.refineryprocessing.interfaces.rest.resources.ReceiveBatchAtRefineryResource;
import com.opaltrace.platform.refineryprocessing.interfaces.rest.resources.RefineryReceiptResource;
import com.opaltrace.platform.refineryprocessing.interfaces.rest.resources.SplitBatchResource;
import com.opaltrace.platform.refineryprocessing.interfaces.rest.transform.RefineryReceiptResourceFromEntityAssembler;
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
@RequestMapping(value = "/api/v1/refinery", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Refinery Processing", description = "Batch reception, splitting and processing endpoints (Plan Platinum)")
public class RefineryBatchesController {

    private final RefineryProcessingCommandService refineryProcessingCommandService;
    private final RefineryProcessingQueryService refineryProcessingQueryService;

    public RefineryBatchesController(RefineryProcessingCommandService refineryProcessingCommandService,
                                     RefineryProcessingQueryService refineryProcessingQueryService) {
        this.refineryProcessingCommandService = refineryProcessingCommandService;
        this.refineryProcessingQueryService = refineryProcessingQueryService;
    }

    @PostMapping("/batches/{batchPk}/receive")
    @Operation(summary = "Receive batch at refinery (US09)", description = "Validates weight discrepancy <=2%, transitions batch EN_TRANSITO->EN_PLANTA, fires BatchReceivedAtRefineryEvent.")
    public ResponseEntity<?> receiveBatch(@PathVariable Long batchPk,
                                          @Valid @RequestBody ReceiveBatchAtRefineryResource resource) {
        var command = new ReceiveBatchAtRefineryCommand(
                batchPk,
                resource.batchId(),
                resource.refineryId(),
                resource.supervisorId(),
                resource.declaredWeightKg()
        );
        var result = refineryProcessingCommandService.handle(command)
                .flatMap(receiptId -> refineryProcessingQueryService.handle(new GetRefineryReceiptByBatchPkQuery(batchPk))
                        .<Result<com.opaltrace.platform.refineryprocessing.domain.model.aggregates.RefineryReceipt, ApplicationError>>
                                map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("RefineryReceipt", batchPk.toString()))));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, RefineryReceiptResourceFromEntityAssembler::toResource, HttpStatus.CREATED);
    }

    @PostMapping("/batches/{batchPk}/split")
    @Operation(summary = "Split batch into sub-lots (US10)", description = "Creates sub-lot MineralBatches. Sum of weights must not exceed parent. Fires ChildBatchCreatedEvent and BatchDividedEvent.")
    public ResponseEntity<?> splitBatch(@PathVariable Long batchPk,
                                        @Valid @RequestBody SplitBatchResource resource) {
        var subLots = resource.subLots().stream()
                .map(sl -> new SplitBatchCommand.SubLotRequest(sl.weightKg(), sl.mineralType()))
                .toList();
        var command = new SplitBatchCommand(batchPk, resource.supervisorId(), subLots);
        var result = refineryProcessingCommandService.handle(command)
                .flatMap(childPks -> refineryProcessingQueryService.handle(new GetRefineryReceiptByBatchPkQuery(batchPk))
                        .<Result<com.opaltrace.platform.refineryprocessing.domain.model.aggregates.RefineryReceipt, ApplicationError>>
                                map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("RefineryReceipt", batchPk.toString()))));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, RefineryReceiptResourceFromEntityAssembler::toResource, HttpStatus.CREATED);
    }

    @PostMapping("/batches/{batchPk}/complete")
    @Operation(summary = "Complete batch processing (US11)", description = "Transitions batch EN_PLANTA->PROCESADO. Validates all sub-lots are PROCESADO if any exist.")
    public ResponseEntity<?> completeProcessing(@PathVariable Long batchPk,
                                                @RequestParam(required = false) Long supervisorId) {
        var command = new CompleteProcessingCommand(batchPk, supervisorId);
        var result = refineryProcessingCommandService.handle(command)
                .flatMap(id -> refineryProcessingQueryService.handle(new GetRefineryReceiptByBatchPkQuery(batchPk))
                        .<Result<com.opaltrace.platform.refineryprocessing.domain.model.aggregates.RefineryReceipt, ApplicationError>>
                                map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("RefineryReceipt", batchPk.toString()))));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, RefineryReceiptResourceFromEntityAssembler::toResource, HttpStatus.OK);
    }

    @GetMapping("/batches/{batchPk}")
    @Operation(summary = "Get refinery receipt by batch PK")
    public ResponseEntity<?> getRefineryReceipt(@PathVariable Long batchPk) {
        var receipt = refineryProcessingQueryService.handle(new GetRefineryReceiptByBatchPkQuery(batchPk));
        if (receipt.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(RefineryReceiptResourceFromEntityAssembler.toResource(receipt.get()));
    }

    @GetMapping("/batches")
    @Operation(summary = "Get all refinery receipts")
    public ResponseEntity<List<RefineryReceiptResource>> getAllRefineryReceipts() {
        var receipts = refineryProcessingQueryService.handle(new GetAllRefineryReceiptsQuery()).stream()
                .map(RefineryReceiptResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(receipts);
    }
}
