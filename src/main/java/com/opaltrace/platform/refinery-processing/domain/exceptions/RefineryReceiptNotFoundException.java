package com.opaltrace.platform.refineryprocessing.domain.exceptions;

public class RefineryReceiptNotFoundException extends RuntimeException {
    public RefineryReceiptNotFoundException(String message) {
        super(message);
    }
}
