package com.opaltrace.platform.custodychain.domain.model.commands;

public record UpdateLocationCommand(
        Long batchPk,
        Double latitude,
        Double longitude,
        Long recordedByUserId,
        int maxRouteMinutes
) {
    public UpdateLocationCommand(Long batchPk, Double latitude, Double longitude, Long recordedByUserId) {
        this(batchPk, latitude, longitude, recordedByUserId, 480);
    }
}
