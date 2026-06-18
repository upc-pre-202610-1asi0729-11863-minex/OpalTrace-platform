package com.opaltrace.platform.custodychain.domain.exceptions;

public class CustodyTransferNotFoundException extends RuntimeException {
    public CustodyTransferNotFoundException(String message) {
        super(message);
    }
}
