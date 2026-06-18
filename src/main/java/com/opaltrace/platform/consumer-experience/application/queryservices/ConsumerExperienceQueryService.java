package com.opaltrace.platform.consumerexperience.application.queryservices;

import com.opaltrace.platform.consumerexperience.domain.model.aggregates.ProductVerification;
import com.opaltrace.platform.consumerexperience.domain.model.entities.TraceabilityPoint;
import com.opaltrace.platform.consumerexperience.domain.model.queries.GetTraceabilityMapByCertificateIdQuery;
import com.opaltrace.platform.consumerexperience.domain.model.queries.GetVerificationByCertificateIdQuery;

import java.util.List;
import java.util.Optional;

public interface ConsumerExperienceQueryService {
    Optional<ProductVerification> handle(GetVerificationByCertificateIdQuery query);
    List<TraceabilityPoint> handle(GetTraceabilityMapByCertificateIdQuery query);
}
