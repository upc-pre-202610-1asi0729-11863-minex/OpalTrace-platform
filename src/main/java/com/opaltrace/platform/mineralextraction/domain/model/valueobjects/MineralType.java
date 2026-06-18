package com.opaltrace.platform.mineralextraction.domain.model.valueobjects;

public enum MineralType {
    GOLD,
    SILVER,
    COPPER,
    PLATINUM,
    OTHER;

    public double maxWeightKg() {
        return switch (this) {
            case GOLD -> 1000.0;
            case SILVER -> 5000.0;
            case COPPER -> 10000.0;
            case PLATINUM -> 500.0;
            case OTHER -> 10000.0;
        };
    }
}
