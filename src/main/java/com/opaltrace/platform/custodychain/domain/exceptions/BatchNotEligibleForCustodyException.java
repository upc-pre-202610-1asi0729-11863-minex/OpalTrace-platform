package com.opaltrace.platform.custodychain.domain.exceptions;

public class BatchNotEligibleForCustodyException extends RuntimeException {
    public BatchNotEligibleForCustodyException(String message) {
        super(message);
    }
}
