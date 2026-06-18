package com.opaltrace.platform.refineryprocessing.domain.model.aggregates;

import com.opaltrace.platform.refineryprocessing.domain.model.commands.ReceiveBatchAtRefineryCommand;
import com.opaltrace.platform.refineryprocessing.domain.model.entities.SubLotRecord;
import com.opaltrace.platform.refineryprocessing.domain.model.events.BatchReceivedAtRefineryEvent;
import com.opaltrace.platform.refineryprocessing.domain.model.events.ProcessingCompletedEvent;
import com.opaltrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RefineryReceipt extends AbstractDomainAggregateRoot<RefineryReceipt> {

    private Long id;
    private Long batchPk;
    private String batchId;
    private Long refineryId;
    private Long supervisorId;
    private double declaredWeightKg;
    private double originalWeightKg;
    private double weightDiscrepancyPercent;
    private LocalDateTime receivedAt;
    private LocalDateTime processingCompletedAt;
    private String blockchainTxHash;
    private List<SubLotRecord> subLots;

    public RefineryReceipt() {
        this.subLots = new ArrayList<>();
    }

    public RefineryReceipt(ReceiveBatchAtRefineryCommand cmd, double originalWeightKg) {
        this.subLots = new ArrayList<>();
        this.batchPk = cmd.batchPk();
        this.batchId = cmd.batchId();
        this.refineryId = cmd.refineryId();
        this.supervisorId = cmd.supervisorId();
        this.declaredWeightKg = cmd.declaredWeightKg();
        this.originalWeightKg = originalWeightKg;
        this.weightDiscrepancyPercent = Math.abs(cmd.declaredWeightKg() - originalWeightKg) / originalWeightKg * 100.0;
        this.receivedAt = LocalDateTime.now();
        this.blockchainTxHash = simulateBlockchainTx();

        registerDomainEvent(new BatchReceivedAtRefineryEvent(
                this.batchId,
                this.batchPk,
                this.refineryId,
                this.supervisorId,
                this.declaredWeightKg,
                this.originalWeightKg,
                this.weightDiscrepancyPercent,
                this.receivedAt
        ));
    }

    public void addSubLot(SubLotRecord subLot) {
        this.subLots.add(subLot);
    }

    public void completeProcessing(double shrinkagePercent) {
        this.processingCompletedAt = LocalDateTime.now();
        registerDomainEvent(new ProcessingCompletedEvent(
                this.batchId,
                this.batchPk,
                this.supervisorId,
                this.processingCompletedAt,
                shrinkagePercent
        ));
    }

    public void reconstitute(Long id, Long batchPk, String batchId, Long refineryId, Long supervisorId,
                             double declaredWeightKg, double originalWeightKg, double weightDiscrepancyPercent,
                             LocalDateTime receivedAt, LocalDateTime processingCompletedAt,
                             String blockchainTxHash, List<SubLotRecord> subLots) {
        this.id = id;
        this.batchPk = batchPk;
        this.batchId = batchId;
        this.refineryId = refineryId;
        this.supervisorId = supervisorId;
        this.declaredWeightKg = declaredWeightKg;
        this.originalWeightKg = originalWeightKg;
        this.weightDiscrepancyPercent = weightDiscrepancyPercent;
        this.receivedAt = receivedAt;
        this.processingCompletedAt = processingCompletedAt;
        this.blockchainTxHash = blockchainTxHash;
        this.subLots = subLots != null ? subLots : new ArrayList<>();
    }

    public void setId(Long id) { this.id = id; }

    private static String simulateBlockchainTx() {
        return "0x" + Long.toHexString(System.currentTimeMillis()) + Long.toHexString((long) (Math.random() * Long.MAX_VALUE));
    }

    public Long getId() { return id; }
    public Long getBatchPk() { return batchPk; }
    public String getBatchId() { return batchId; }
    public Long getRefineryId() { return refineryId; }
    public Long getSupervisorId() { return supervisorId; }
    public double getDeclaredWeightKg() { return declaredWeightKg; }
    public double getOriginalWeightKg() { return originalWeightKg; }
    public double getWeightDiscrepancyPercent() { return weightDiscrepancyPercent; }
    public LocalDateTime getReceivedAt() { return receivedAt; }
    public LocalDateTime getProcessingCompletedAt() { return processingCompletedAt; }
    public String getBlockchainTxHash() { return blockchainTxHash; }
    public List<SubLotRecord> getSubLots() { return subLots; }
}
