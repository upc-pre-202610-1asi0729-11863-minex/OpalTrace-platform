package com.opaltrace.platform.analytics.interfaces.rest;

import com.opaltrace.platform.analytics.application.queryservices.AnalyticsQueryService;
import com.opaltrace.platform.analytics.domain.model.queries.*;
import com.opaltrace.platform.analytics.interfaces.rest.resources.ComparativeMetricsResource;
import com.opaltrace.platform.analytics.interfaces.rest.resources.DashboardMetricsResource;
import com.opaltrace.platform.analytics.interfaces.rest.resources.ShrinkageIndicatorResource;
import com.opaltrace.platform.analytics.interfaces.rest.transform.AnalyticsResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/analytics")
@Tag(name = "Analytics & Reporting", description = "Operational dashboard and metrics endpoints")
public class AnalyticsDashboardController {

    private final AnalyticsQueryService analyticsQueryService;

    public AnalyticsDashboardController(AnalyticsQueryService analyticsQueryService) {
        this.analyticsQueryService = analyticsQueryService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardMetricsResource> getDashboardMetrics(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String mineralType) {
        var query = new GetDashboardMetricsQuery(userId, fromDate, toDate, mineralType);
        var metrics = analyticsQueryService.handle(query);
        return new ResponseEntity<>(AnalyticsResourceFromEntityAssembler.toDashboardResource(metrics), HttpStatus.OK);
    }

    @GetMapping("/shrinkage")
    public ResponseEntity<List<ShrinkageIndicatorResource>> getShrinkageIndicators(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        var query = new GetShrinkageIndicatorsQuery(userId, fromDate, toDate);
        var indicators = analyticsQueryService.handle(query);
        var resources = indicators.stream()
                .map(AnalyticsResourceFromEntityAssembler::toShrinkageResource)
                .collect(Collectors.toList());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping("/comparative")
    public ResponseEntity<ComparativeMetricsResource> getComparativeAnalysis(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period1Start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period1End,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period2Start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period2End) {
        var query = new GetComparativeAnalysisQuery(userId, period1Start, period1End, period2Start, period2End);
        var metrics = analyticsQueryService.handle(query);
        return new ResponseEntity<>(AnalyticsResourceFromEntityAssembler.toComparativeResource(metrics), HttpStatus.OK);
    }
}
