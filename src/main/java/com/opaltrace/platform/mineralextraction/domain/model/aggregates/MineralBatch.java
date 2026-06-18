package com.opaltrace.platform.mineralextraction.domain.model.aggregates;

import com.opaltrace.platform.mineralextraction.domain.model.commands.RegisterMineralBatchCommand;
import com.opaltrace.platform.mineralextraction.domain.model.entities.AnomalyReport;
import com.opaltrace.platform.mineralextraction.domain.model.events.AnomalyDetectedEvent;
import com.opaltrace.platform.mineralextraction.domain.model.events.MineralExtractedEvent;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.*;
import com.opaltrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MineralBatch extends AbstractDomainAggregateRoot<MineralBatch> {

    private Long id;
    private String batchId;
    private MineralType mineralType;
    private WeightKg weight;
    private GpsCoordinate originCoordinates;
    private BatchStatus status;
    private boolean blocked;
    private Long supervisorId;
    private Long miningCompanyId;
    private String blockchainTxHash;
    private Long parentBatchId;
    private String qrCodeData;
    private List<AnomalyReport> anomalyReports;

    public MineralBatch() {
        this.anomalyReports = new ArrayList<>();
    }

    public MineralBatch(RegisterMineralBatchCommand command, String generatedBatchId) {
        this.anomalyReports = new ArrayList<>();
        var weight = new WeightKg(command.weightKg());
        weight.validateMaxFor(command.mineralType());

        this.batchId = generatedBatchId;
        this.mineralType = command.mineralType();
        this.weight = weight;
        this.originCoordinates = new GpsCoordinate(command.latitude(), command.longitude());
        this.status = BatchStatus.EN_ORIGEN;
        this.blocked = false;
        this.supervisorId = command.supervisorId();
        this.miningCompanyId = command.miningCompanyId();
        this.blockchainTxHash = simulateBlockchainTx();

        registerDomainEvent(new MineralExtractedEvent(
                this.batchId,
                this.mineralType,
                this.weight.value(),
                this.originCoordinates.latitude(),
                this.originCoordinates.longitude(),
                this.supervisorId,
                this.miningCompanyId,
                LocalDateTime.now()
        ));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void reconstitute(String batchId, MineralType mineralType, WeightKg weight,
                             GpsCoordinate originCoordinates, BatchStatus status, boolean blocked,
                             Long supervisorId, Long miningCompanyId, String blockchainTxHash,
                             Long parentBatchId, String qrCodeData, List<AnomalyReport> anomalyReports) {
        this.batchId = batchId;
        this.mineralType = mineralType;
        this.weight = weight;
        this.originCoordinates = originCoordinates;
        this.status = status;
        this.blocked = blocked;
        this.supervisorId = supervisorId;
        this.miningCompanyId = miningCompanyId;
        this.blockchainTxHash = blockchainTxHash;
        this.parentBatchId = parentBatchId;
        this.qrCodeData = qrCodeData;
        this.anomalyReports = anomalyReports != null ? anomalyReports : new ArrayList<>();
    }

    public void reportAnomaly(AnomalyReport anomaly) {
        if (this.blocked)
            throw new IllegalStateException("Batch %s is already blocked".formatted(batchId));
        this.anomalyReports.add(anomaly);
        this.blocked = true;
        registerDomainEvent(new AnomalyDetectedEvent(
                this.batchId,
                anomaly.getCategory(),
                anomaly.getDescription(),
                anomaly.getReportedByUserId(),
                LocalDateTime.now()
        ));
    }

    public void generateQr() {
        if (this.blocked)
            throw new IllegalStateException("Cannot generate QR: batch is blocked by an active anomaly");
        if (this.batchId == null || this.mineralType == null)
            throw new IllegalStateException("Cannot generate QR: batch traceability is incomplete (missing required fields)");
        this.qrCodeData = "https://opaltrace.com/verify/" + this.batchId;
    }

    public void transitionStatus(BatchStatus newStatus) {
        if (this.blocked)
            throw new IllegalStateException("Batch %s is blocked and cannot change status".formatted(batchId));
        if (!this.status.canTransitionTo(newStatus))
            throw new IllegalStateException(
                    "Invalid state transition from %s to %s".formatted(this.status, newStatus));
        this.status = newStatus;
    }

    public boolean hasActiveAnomalies() {
        return anomalyReports.stream().anyMatch(r -> !r.isResolved());
    }

    private static String simulateBlockchainTx() {
        return "0x" + Long.toHexString(System.currentTimeMillis()) + Long.toHexString((long)(Math.random() * Long.MAX_VALUE));
    }

    public static String generateBatchId(int sequence) {
        int year = Year.now().getValue();
        return "OT-%d-%04d".formatted(year, sequence);
    }
}
