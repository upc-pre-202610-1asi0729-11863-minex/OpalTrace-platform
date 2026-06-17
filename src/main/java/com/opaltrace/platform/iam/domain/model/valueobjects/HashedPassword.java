package com.opaltrace.platform.iam.domain.model.valueobjects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashedPassword {

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private final String hash;

    private HashedPassword(String hash) {
        this.hash = hash;
    }

    public static HashedPassword fromRaw(String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 8)
            throw new IllegalArgumentException("Password must be at least 8 characters");
        return new HashedPassword(ENCODER.encode(rawPassword));
    }

    public static HashedPassword fromHash(String existingHash) {
        return new HashedPassword(existingHash);
    }

    public boolean matches(String rawPassword) {
        return ENCODER.matches(rawPassword, this.hash);
    }

    public String value() {
        return hash;
    }

    @Override
    public String toString() {
        return "[PROTECTED]";
    }
}
