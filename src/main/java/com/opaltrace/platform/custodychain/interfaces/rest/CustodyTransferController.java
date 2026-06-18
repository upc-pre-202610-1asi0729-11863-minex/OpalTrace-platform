package com.opaltrace.platform.custodychain.interfaces.rest;

import com.opaltrace.platform.custodychain.application.commandservices.CustodyChainCommandService;
import com.opaltrace.platform.custodychain.application.queryservices.CustodyChainQueryService;
import com.opaltrace.platform.custodychain.domain.model.commands.AcceptCustodyCommand;
import com.opaltrace.platform.custodychain.domain.model.commands.UpdateLocationCommand;
import com.opaltrace.platform.custodychain.domain.model.queries.*;
import com.opaltrace.platform.custodychain.interfaces.rest.resources.*;
import com.opaltrace.platform.custodychain.interfaces.rest.transform.CustodyTransferResourceFromEntityAssembler;
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
@Tag(name = "Custody Chain", description = "Custody transfer and location tracking endpoints")
public class CustodyTransferController {

    private final CustodyChainCommandService custodyChainCommandService;
    private final CustodyChainQueryService custodyChainQueryService;

    public CustodyTransferController(CustodyChainCommandService custodyChainCommandService,
                                     CustodyChainQueryService custodyChainQueryService) {
        this.custodyChainCommandService = custodyChainCommandService;
        this.custodyChainQueryService = custodyChainQueryService;
    }

    @PostMapping(value = "/api/v1/mineral-batches/{batchPk}/custody/accept", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Accept custody of a batch (US07)", description = "Accepts custody of a batch in EN_ORIGEN status. Transitions batch to EN_TRANSITO and fires TransportStartedEvent.")
    public ResponseEntity<?> acceptCustody(@PathVariable Long batchPk,
                                           @Valid @RequestBody AcceptCustodyResource resource) {
        var command = new AcceptCustodyCommand(
                batchPk,
                resource.batchId(),
                resource.custodyHolderUserId(),
                resource.latitude(),
                resource.longitude()
        );
        var result = custodyChainCommandService.handle(command)
                .flatMap(transferId -> custodyChainQueryService.handle(new GetCustodyTransferByBatchPkQuery(batchPk))
                        .<Result<com.opaltrace.platform.custodychain.domain.model.aggregates.CustodyTransfer, ApplicationError>>
                                map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("CustodyTransfer", batchPk.toString()))));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, CustodyTransferResourceFromEntityAssembler::toResource, HttpStatus.CREATED);
    }

    @PostMapping(value = "/api/v1/mineral-batches/{batchPk}/custody/location", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Update GPS location during transit (US08)", description = "Updates GPS location for a batch in EN_TRANSITO. Auto-generates DelayedTransportAlert if maxRouteMinutes exceeded.")
    public ResponseEntity<?> updateLocation(@PathVariable Long batchPk,
                                            @Valid @RequestBody UpdateLocationResource resource) {
        var command = new UpdateLocationCommand(
                batchPk,
                resource.latitude(),
                resource.longitude(),
                resource.recordedByUserId(),
                resource.maxRouteMinutes()
        );
        var result = custodyChainCommandService.handle(command)
                .flatMap(transferId -> custodyChainQueryService.handle(new GetCustodyTransferByBatchPkQuery(batchPk))
                        .<Result<com.opaltrace.platform.custodychain.domain.model.aggregates.CustodyTransfer, ApplicationError>>
                                map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("CustodyTransfer", batchPk.toString()))));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, CustodyTransferResourceFromEntityAssembler::toResource, HttpStatus.OK);
    }

    @GetMapping(value = "/api/v1/mineral-batches/{batchPk}/custody", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get custody transfer by batch PK")
    public ResponseEntity<?> getCustodyTransfer(@PathVariable Long batchPk) {
        var transfer = custodyChainQueryService.handle(new GetCustodyTransferByBatchPkQuery(batchPk));
        if (transfer.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(CustodyTransferResourceFromEntityAssembler.toResource(transfer.get()));
    }

    @GetMapping(value = "/api/v1/mineral-batches/{batchPk}/custody/location-history", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get location history for a batch in transit")
    public ResponseEntity<List<LocationUpdateResource>> getLocationHistory(@PathVariable Long batchPk) {
        var updates = custodyChainQueryService.handle(new GetLocationHistoryByBatchPkQuery(batchPk)).stream()
                .map(lu -> new LocationUpdateResource(
                        lu.getId(),
                        lu.getLatitude(),
                        lu.getLongitude(),
                        lu.getRecordedAt(),
                        lu.getRecordedByUserId()))
                .toList();
        return ResponseEntity.ok(updates);
    }

    @GetMapping(value = "/api/v1/custody-transfers", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all custody transfers")
    public ResponseEntity<List<CustodyTransferResource>> getAllCustodyTransfers() {
        var transfers = custodyChainQueryService.handle(new GetAllCustodyTransfersQuery()).stream()
                .map(CustodyTransferResourceFromEntityAssembler::toResource)
                .toList();
        return ResponseEntity.ok(transfers);
    }
}
