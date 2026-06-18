package com.opaltrace.platform.custodychain.domain.model.aggregates;

import com.opaltrace.platform.custodychain.domain.model.commands.AcceptCustodyCommand;
import com.opaltrace.platform.custodychain.domain.model.entities.LocationUpdate;
import com.opaltrace.platform.custodychain.domain.model.events.TransportStartedEvent;
import com.opaltrace.platform.custodychain.domain.model.valueobjects.CustodyStatus;
import com.opaltrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustodyTransfer extends AbstractDomainAggregateRoot<CustodyTransfer> {

    private Long id;
    private String batchId;
    private Long batchPk;
    private Long custodyHolderUserId;
    private CustodyStatus status;
    private Double startLatitude;
    private Double startLongitude;
    private Double endLatitude;
    private Double endLongitude;
    private String blockchainTxHash;
    private List<LocationUpdate> locationUpdates;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    public CustodyTransfer() {
        this.locationUpdates = new ArrayList<>();
    }

    public CustodyTransfer(AcceptCustodyCommand cmd) {
        this.locationUpdates = new ArrayList<>();
        this.batchId = cmd.batchId();
        this.batchPk = cmd.batchPk();
        this.custodyHolderUserId = cmd.custodyHolderUserId();
        this.status = CustodyStatus.IN_TRANSIT;
        this.startLatitude = cmd.latitude();
        this.startLongitude = cmd.longitude();
        this.startedAt = LocalDateTime.now();
        this.blockchainTxHash = simulateBlockchainTx();

        registerDomainEvent(new TransportStartedEvent(
                this.batchId,
                this.batchPk,
                this.custodyHolderUserId,
                this.startLatitude,
                this.startLongitude,
                this.startedAt
        ));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addLocationUpdate(LocationUpdate update) {
        this.locationUpdates.add(update);
    }

    public void complete(double endLat, double endLon) {
        this.status = CustodyStatus.DELIVERED;
        this.endLatitude = endLat;
        this.endLongitude = endLon;
        this.completedAt = LocalDateTime.now();
    }

    public void reconstitute(Long id, String batchId, Long batchPk, Long custodyHolderUserId,
                             CustodyStatus status, Double startLatitude, Double startLongitude,
                             Double endLatitude, Double endLongitude, String blockchainTxHash,
                             List<LocationUpdate> locationUpdates, LocalDateTime startedAt,
                             LocalDateTime completedAt) {
        this.id = id;
        this.batchId = batchId;
        this.batchPk = batchPk;
        this.custodyHolderUserId = custodyHolderUserId;
        this.status = status;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.blockchainTxHash = blockchainTxHash;
        this.locationUpdates = locationUpdates != null ? locationUpdates : new ArrayList<>();
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }

    private static String simulateBlockchainTx() {
        return "0x" + Long.toHexString(System.currentTimeMillis()) + Long.toHexString((long) (Math.random() * Long.MAX_VALUE));
    }

    public Long getId() { return id; }
    public String getBatchId() { return batchId; }
    public Long getBatchPk() { return batchPk; }
    public Long getCustodyHolderUserId() { return custodyHolderUserId; }
    public CustodyStatus getStatus() { return status; }
    public Double getStartLatitude() { return startLatitude; }
    public Double getStartLongitude() { return startLongitude; }
    public Double getEndLatitude() { return endLatitude; }
    public Double getEndLongitude() { return endLongitude; }
    public String getBlockchainTxHash() { return blockchainTxHash; }
    public List<LocationUpdate> getLocationUpdates() { return locationUpdates; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
}
