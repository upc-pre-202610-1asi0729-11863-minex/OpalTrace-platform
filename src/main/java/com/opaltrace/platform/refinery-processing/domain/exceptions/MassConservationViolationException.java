package com.opaltrace.platform.refineryprocessing.domain.exceptions;

public class MassConservationViolationException extends RuntimeException {
    public MassConservationViolationException(String message) {
        super(message);
    }
}
