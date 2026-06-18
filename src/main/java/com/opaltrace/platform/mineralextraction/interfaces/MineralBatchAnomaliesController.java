package com.opaltrace.platform.mineralextraction.interfaces.rest;

import com.opaltrace.platform.mineralextraction.application.commandservices.MineralBatchCommandService;
import com.opaltrace.platform.mineralextraction.application.queryservices.MineralBatchQueryService;
import com.opaltrace.platform.mineralextraction.domain.model.commands.ReportAnomalyCommand;
import com.opaltrace.platform.mineralextraction.domain.model.queries.GetAnomalyAlertsByBatchQuery;
import com.opaltrace.platform.mineralextraction.domain.model.queries.GetBatchByIdQuery;
import com.opaltrace.platform.mineralextraction.interfaces.rest.resources.AnomalyReportResource;
import com.opaltrace.platform.mineralextraction.interfaces.rest.resources.MineralBatchResource;
import com.opaltrace.platform.mineralextraction.interfaces.rest.resources.ReportAnomalyResource;
import com.opaltrace.platform.mineralextraction.interfaces.rest.transform.MineralBatchResourceFromEntityAssembler;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import com.opaltrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping(value = "/api/v1/mineral-batches/{batchPk}/anomalies", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Anomaly Reports", description = "Anomaly reporting and alert endpoints for mineral batches")
public class MineralBatchAnomaliesController {

    private final MineralBatchCommandService mineralBatchCommandService;
    private final MineralBatchQueryService mineralBatchQueryService;

    public MineralBatchAnomaliesController(MineralBatchCommandService mineralBatchCommandService,
                                           MineralBatchQueryService mineralBatchQueryService) {
        this.mineralBatchCommandService = mineralBatchCommandService;
        this.mineralBatchQueryService = mineralBatchQueryService;
    }

    @PostMapping
    @Operation(summary = "Report anomaly on batch (US04)", description = "Reports an anomaly on a batch, automatically blocking it (isBlocked=true) and registering AnomalyDetected blockchain event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Anomaly reported and batch blocked",
                    content = @Content(schema = @Schema(implementation = MineralBatchResource.class))),
            @ApiResponse(responseCode = "404", description = "Batch not found"),
            @ApiResponse(responseCode = "422", description = "Batch already blocked")
    })
    public ResponseEntity<?> reportAnomaly(
            @PathVariable Long batchPk,
            @Valid @RequestBody ReportAnomalyResource resource) {
        var command = new ReportAnomalyCommand(
                batchPk,
                resource.description(),
                resource.category(),
                resource.photoEvidenceUrl(),
                resource.reportedByUserId()
        );
        var result = mineralBatchCommandService.handle(command)
                .flatMap(pk -> mineralBatchQueryService.handle(new GetBatchByIdQuery(pk))
                        .<Result<com.opaltrace.platform.mineralextraction.domain.model.aggregates.MineralBatch, ApplicationError>>
                                map(Result::success)
                        .orElseGet(() -> Result.failure(ApplicationError.notFound("MineralBatch", pk.toString()))));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, MineralBatchResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get anomaly alerts for batch (US05)", description = "Retrieves all anomaly reports associated with a batch.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alerts retrieved",
                    content = @Content(schema = @Schema(implementation = AnomalyReportResource.class)))
    })
    public ResponseEntity<List<AnomalyReportResource>> getAnomalyAlerts(@PathVariable Long batchPk) {
        var alerts = mineralBatchQueryService.handle(new GetAnomalyAlertsByBatchQuery(batchPk)).stream()
                .map(MineralBatchResourceFromEntityAssembler::toAnomalyResource)
                .toList();
        return ResponseEntity.ok(alerts);
    }
}
