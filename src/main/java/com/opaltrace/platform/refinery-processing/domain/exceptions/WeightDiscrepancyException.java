package com.opaltrace.platform.refineryprocessing.domain.exceptions;

public class WeightDiscrepancyException extends RuntimeException {
    private final String batchId;
    private final double declaredKg;
    private final double originalKg;
    private final double discrepancyPercent;

    public WeightDiscrepancyException(String batchId, double declaredKg, double originalKg, double discrepancyPercent) {
        super("Weight discrepancy for batch %s: declared %.2fkg vs original %.2fkg (%.2f%%)".formatted(
                batchId, declaredKg, originalKg, discrepancyPercent));
        this.batchId = batchId;
        this.declaredKg = declaredKg;
        this.originalKg = originalKg;
        this.discrepancyPercent = discrepancyPercent;
    }

    public String getBatchId() { return batchId; }
    public double getDeclaredKg() { return declaredKg; }
    public double getOriginalKg() { return originalKg; }
    public double getDiscrepancyPercent() { return discrepancyPercent; }
}
