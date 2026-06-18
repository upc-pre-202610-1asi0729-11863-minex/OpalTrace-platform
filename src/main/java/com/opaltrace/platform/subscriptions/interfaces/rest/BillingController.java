package com.opaltrace.platform.subscriptions.interfaces.rest;

import com.opaltrace.platform.subscriptions.application.queryservices.SubscriptionQueryService;
import com.opaltrace.platform.subscriptions.domain.model.queries.GetBillingHistoryByUserIdQuery;
import com.opaltrace.platform.subscriptions.interfaces.rest.resources.BillingRecordResource;
import com.opaltrace.platform.subscriptions.interfaces.rest.resources.InvoiceResource;
import com.opaltrace.platform.subscriptions.interfaces.rest.transform.SubscriptionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/billing")
@Tag(name = "Billing", description = "Billing history and invoice download endpoints")
public class BillingController {

    private final SubscriptionQueryService subscriptionQueryService;

    public BillingController(SubscriptionQueryService subscriptionQueryService) {
        this.subscriptionQueryService = subscriptionQueryService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BillingRecordResource>> getBillingHistory(@PathVariable Long userId) {
        var query = new GetBillingHistoryByUserIdQuery(userId);
        var records = subscriptionQueryService.handle(query);
        var resources = records.stream()
                .map(SubscriptionResourceFromEntityAssembler::toBillingResource)
                .collect(Collectors.toList());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/invoice/{invoiceNumber}")
    public ResponseEntity<InvoiceResource> getInvoice(@PathVariable Long userId,
                                                       @PathVariable String invoiceNumber) {
        var query = new GetBillingHistoryByUserIdQuery(userId);
        var records = subscriptionQueryService.handle(query);
        var record = records.stream()
                .filter(r -> invoiceNumber.equals(r.getInvoiceNumber()))
                .findFirst();
        return record.map(r -> new ResponseEntity<>(
                SubscriptionResourceFromEntityAssembler.toInvoiceResource(r, userId), HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }
}
