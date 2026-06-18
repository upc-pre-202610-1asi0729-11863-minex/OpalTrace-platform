package com.opaltrace.platform.mineralextraction.domain.model.valueobjects;

public record WeightKg(double value) {

    public WeightKg {
        if (value <= 0)
            throw new IllegalArgumentException("Weight must be greater than zero, got: " + value);
    }

    public void validateMaxFor(MineralType type) {
        if (value > type.maxWeightKg())
            throw new IllegalArgumentException(
                    "Weight %.2f kg exceeds maximum allowed for %s (max: %.0f kg)".formatted(value, type.name(), type.maxWeightKg()));
    }
}
