package com.opaltrace.platform.mineralextraction.domain.exceptions;

public class UnauthorizedZoneException extends RuntimeException {
    public UnauthorizedZoneException(double latitude, double longitude) {
        super("Origin outside authorized zone — Coordinates: [%s, %s]".formatted(latitude, longitude));
    }
}
