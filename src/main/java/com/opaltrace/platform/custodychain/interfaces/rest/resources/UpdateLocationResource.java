package com.opaltrace.platform.custodychain.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

public record UpdateLocationResource(
        Long recordedByUserId,
        @NotNull Double latitude,
        @NotNull Double longitude,
        int maxRouteMinutes
) {
    public UpdateLocationResource(Long recordedByUserId, Double latitude, Double longitude) {
        this(recordedByUserId, latitude, longitude, 480);
    }
}
