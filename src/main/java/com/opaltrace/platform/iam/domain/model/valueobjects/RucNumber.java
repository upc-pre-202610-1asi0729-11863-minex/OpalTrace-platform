package com.opaltrace.platform.iam.domain.model.valueobjects;

public record RucNumber(String value) {

    public RucNumber {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("RUC must not be blank");
        if (!value.matches("\\d{11}"))
            throw new IllegalArgumentException("RUC must be exactly 11 digits: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
