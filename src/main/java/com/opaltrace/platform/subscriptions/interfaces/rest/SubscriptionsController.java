package com.opaltrace.platform.subscriptions.interfaces.rest;

import com.opaltrace.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.opaltrace.platform.subscriptions.application.commandservices.SubscriptionCommandService;
import com.opaltrace.platform.subscriptions.application.queryservices.SubscriptionQueryService;
import com.opaltrace.platform.subscriptions.domain.model.commands.*;
import com.opaltrace.platform.subscriptions.domain.model.queries.*;
import com.opaltrace.platform.subscriptions.interfaces.rest.resources.*;
import com.opaltrace.platform.subscriptions.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/subscriptions")
@Tag(name = "Subscriptions", description = "Subscription lifecycle management - Silver/Gold/Platinum plans")
public class SubscriptionsController {

    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;

    public SubscriptionsController(SubscriptionCommandService subscriptionCommandService,
                                   SubscriptionQueryService subscriptionQueryService) {
        this.subscriptionCommandService = subscriptionCommandService;
        this.subscriptionQueryService = subscriptionQueryService;
    }

    @PostMapping
    public ResponseEntity<?> activateSubscription(@Valid @RequestBody ActivateSubscriptionResource resource) {
        var command = new ActivateSubscriptionCommand(resource.userId(), resource.planTier(),
                resource.billingCycle(), resource.paymentMethodToken(), resource.amount());
        var result = subscriptionCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, id -> {
            var sub = subscriptionQueryService.handle(new GetSubscriptionByUserIdQuery(resource.userId()));
            return sub.map(SubscriptionResourceFromEntityAssembler::toResource).orElse(null);
        }, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SubscriptionResource> getByUser(@PathVariable Long userId) {
        var query = new GetSubscriptionByUserIdQuery(userId);
        var subscription = subscriptionQueryService.handle(query);
        return subscription.map(s -> new ResponseEntity<>(SubscriptionResourceFromEntityAssembler.toResource(s), HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{subscriptionId}/upgrade")
    public ResponseEntity<?> upgradePlan(@PathVariable Long subscriptionId,
                                          @Valid @RequestBody UpgradePlanResource resource) {
        var command = new UpgradePlanCommand(subscriptionId, resource.userId(),
                resource.newTier(), resource.paymentMethodToken());
        var result = subscriptionCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, id -> {
            var sub = subscriptionQueryService.handle(new GetActiveSubscriptionByUserIdQuery(resource.userId()));
            return sub.map(SubscriptionResourceFromEntityAssembler::toResource).orElse(null);
        }, HttpStatus.OK);
    }

    @PutMapping("/{subscriptionId}/downgrade")
    public ResponseEntity<?> downgradePlan(@PathVariable Long subscriptionId,
                                            @Valid @RequestBody DowngradePlanResource resource) {
        var command = new DowngradePlanCommand(subscriptionId, resource.userId(), resource.targetTier());
        var result = subscriptionCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, id -> {
            var sub = subscriptionQueryService.handle(new GetSubscriptionByUserIdQuery(resource.userId()));
            return sub.map(SubscriptionResourceFromEntityAssembler::toResource).orElse(null);
        }, HttpStatus.OK);
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<?> cancelSubscription(@PathVariable Long subscriptionId,
                                                 @RequestBody(required = false) CancelSubscriptionResource resource) {
        Long userId = resource != null ? resource.userId() : null;
        String reason = resource != null ? resource.cancellationReason() : null;
        var command = new CancelSubscriptionCommand(subscriptionId, userId, reason);
        var result = subscriptionCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, id -> {
            var sub = subscriptionQueryService.handle(new GetSubscriptionByUserIdQuery(userId));
            return sub.map(SubscriptionResourceFromEntityAssembler::toResource).orElse(null);
        }, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResource>> getAllSubscriptions() {
        var subscriptions = subscriptionQueryService.handle(new GetAllSubscriptionsQuery());
        var resources = subscriptions.stream()
                .map(SubscriptionResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }
}
