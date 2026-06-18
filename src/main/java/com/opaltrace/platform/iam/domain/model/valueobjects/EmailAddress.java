package com.opaltrace.platform.iam.domain.model.valueobjects;

import java.util.regex.Pattern;

public record EmailAddress(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public EmailAddress {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Email address must not be blank");
        if (!EMAIL_PATTERN.matcher(value).matches())
            throw new IllegalArgumentException("Invalid email address format: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}
