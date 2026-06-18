package com.opaltrace.platform.consumerexperience.domain.model.entities;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TraceabilityPoint {

    private Long id;
    private String eventType;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
    private String actorName;
    private String blockchainTxHash;
    private int sequenceOrder;

    public TraceabilityPoint() {
    }

    public TraceabilityPoint(String eventType, Double latitude, Double longitude, LocalDateTime timestamp,
                              String actorName, String blockchainTxHash, int sequenceOrder) {
        this.eventType = eventType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.actorName = actorName;
        this.blockchainTxHash = blockchainTxHash;
        this.sequenceOrder = sequenceOrder;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void reconstitute(Long id, String eventType, Double latitude, Double longitude,
                             LocalDateTime timestamp, String actorName, String blockchainTxHash, int sequenceOrder) {
        this.id = id;
        this.eventType = eventType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.actorName = actorName;
        this.blockchainTxHash = blockchainTxHash;
        this.sequenceOrder = sequenceOrder;
    }
}
