package com.opaltrace.platform.consumerexperience.interfaces.rest;

import com.opaltrace.platform.consumerexperience.application.commandservices.ConsumerExperienceCommandService;
import com.opaltrace.platform.consumerexperience.application.queryservices.ConsumerExperienceQueryService;
import com.opaltrace.platform.consumerexperience.domain.model.commands.RecordTraceabilityViewCommand;
import com.opaltrace.platform.consumerexperience.domain.model.commands.VerifyProductAuthenticityCommand;
import com.opaltrace.platform.consumerexperience.domain.model.queries.GetTraceabilityMapByCertificateIdQuery;
import com.opaltrace.platform.consumerexperience.interfaces.rest.resources.ProductVerificationResultResource;
import com.opaltrace.platform.consumerexperience.interfaces.rest.resources.TraceabilityPointResource;
import com.opaltrace.platform.consumerexperience.interfaces.rest.resources.VerifyProductResource;
import com.opaltrace.platform.consumerexperience.interfaces.rest.transform.ProductVerificationResourceFromEntityAssembler;
import com.opaltrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/verify", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Consumer Experience", description = "Public product authenticity verification endpoints - no authentication required")
public class ProductVerificationController {

    private final ConsumerExperienceCommandService commandService;
    private final ConsumerExperienceQueryService queryService;

    public ProductVerificationController(ConsumerExperienceCommandService commandService,
                                          ConsumerExperienceQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping("/{certificateId}/verify")
    @Operation(summary = "Verify product authenticity (US16)", description = "Public endpoint. Verify the authenticity of a product by its certificate ID. Detects suspicious verification attempts.")
    public ResponseEntity<?> verifyProduct(@PathVariable String certificateId,
                                            @RequestBody(required = false) VerifyProductResource resource,
                                            HttpServletRequest request) {
        String ip = resource != null && resource.verifierIp() != null
                ? resource.verifierIp()
                : request.getRemoteAddr();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long consumerId = (auth != null && auth.getPrincipal() instanceof Long)
                ? (Long) auth.getPrincipal() : null;
        var command = new VerifyProductAuthenticityCommand(certificateId, ip, consumerId);
        var result = commandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ProductVerificationResourceFromEntityAssembler::toResource, HttpStatus.OK);
    }

    @GetMapping("/{certificateId}/traceability-map")
    @Operation(summary = "View geographic traceability map (US17)", description = "Public endpoint. Returns ordered list of GPS points with event type, timestamp, actor, blockchain hash.")
    public ResponseEntity<List<TraceabilityPointResource>> getTraceabilityMap(@PathVariable String certificateId,
                                                                               HttpServletRequest request) {
        var recordCmd = new RecordTraceabilityViewCommand(certificateId, request.getRemoteAddr());
        commandService.handle(recordCmd);
        var points = queryService.handle(new GetTraceabilityMapByCertificateIdQuery(certificateId)).stream()
                .map(ProductVerificationResourceFromEntityAssembler::toTraceabilityPointResource)
                .toList();
        return ResponseEntity.ok(points);
    }
}
