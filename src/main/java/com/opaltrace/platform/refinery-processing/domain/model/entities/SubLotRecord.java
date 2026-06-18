package com.opaltrace.platform.refineryprocessing.domain.model.entities;

import java.time.LocalDateTime;

public class SubLotRecord {
    private Long id;
    private Long parentBatchPk;
    private Long childBatchPk;
    private String childBatchId;
    private double weightKg;
    private LocalDateTime createdAt;

    public SubLotRecord() {}

    public SubLotRecord(Long parentBatchPk, Long childBatchPk, String childBatchId, double weightKg) {
        this.parentBatchPk = parentBatchPk;
        this.childBatchPk = childBatchPk;
        this.childBatchId = childBatchId;
        this.weightKg = weightKg;
        this.createdAt = LocalDateTime.now();
    }

    public void reconstitute(Long id, Long parentBatchPk, Long childBatchPk, String childBatchId,
                             double weightKg, LocalDateTime createdAt) {
        this.id = id;
        this.parentBatchPk = parentBatchPk;
        this.childBatchPk = childBatchPk;
        this.childBatchId = childBatchId;
        this.weightKg = weightKg;
        this.createdAt = createdAt;
    }

    public void setId(Long id) { this.id = id; }

    public Long getId() { return id; }
    public Long getParentBatchPk() { return parentBatchPk; }
    public Long getChildBatchPk() { return childBatchPk; }
    public String getChildBatchId() { return childBatchId; }
    public double getWeightKg() { return weightKg; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
