package com.opaltrace.platform.analytics.interfaces.rest;

import com.opaltrace.platform.analytics.application.commandservices.AnalyticsCommandService;
import com.opaltrace.platform.analytics.application.queryservices.AnalyticsQueryService;
import com.opaltrace.platform.analytics.domain.model.commands.GenerateEsgReportCommand;
import com.opaltrace.platform.analytics.domain.model.queries.GetAllReportsQuery;
import com.opaltrace.platform.analytics.domain.model.queries.GetEsgReportQuery;
import com.opaltrace.platform.analytics.interfaces.rest.resources.AnalyticsReportResource;
import com.opaltrace.platform.analytics.interfaces.rest.resources.GenerateEsgReportResource;
import com.opaltrace.platform.analytics.interfaces.rest.transform.AnalyticsResourceFromEntityAssembler;
import com.opaltrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/analytics/esg")
@Tag(name = "ESG Reports", description = "ESG compliance reporting endpoints (Plan Platinum)")
public class EsgReportsController {

    private final AnalyticsCommandService analyticsCommandService;
    private final AnalyticsQueryService analyticsQueryService;

    public EsgReportsController(AnalyticsCommandService analyticsCommandService,
                                AnalyticsQueryService analyticsQueryService) {
        this.analyticsCommandService = analyticsCommandService;
        this.analyticsQueryService = analyticsQueryService;
    }

    @PostMapping
    public ResponseEntity<?> generateEsgReport(@Valid @RequestBody GenerateEsgReportResource resource) {
        var command = new GenerateEsgReportCommand(resource.requestedByUserId(), resource.periodStart(), resource.periodEnd());
        var result = analyticsCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, reportId -> {
            var report = analyticsQueryService.handle(new GetEsgReportQuery(reportId));
            return report.map(AnalyticsResourceFromEntityAssembler::toResource).orElse(null);
        }, HttpStatus.CREATED);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<AnalyticsReportResource> getEsgReport(@PathVariable Long reportId) {
        var query = new GetEsgReportQuery(reportId);
        var report = analyticsQueryService.handle(query);
        return report.map(r -> new ResponseEntity<>(AnalyticsResourceFromEntityAssembler.toResource(r), HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AnalyticsReportResource>> getAllReports() {
        var reports = analyticsQueryService.handle(new GetAllReportsQuery());
        var resources = reports.stream()
                .map(AnalyticsResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }
}
