package com.opaltrace.platform.custodychain.domain.model.entities;

import java.time.LocalDateTime;

public class LocationUpdate {
    private Long id;
    private double latitude;
    private double longitude;
    private LocalDateTime recordedAt;
    private Long recordedByUserId;

    public LocationUpdate(double latitude, double longitude, Long recordedByUserId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.recordedByUserId = recordedByUserId;
        this.recordedAt = LocalDateTime.now();
    }

    public LocationUpdate() {}

    public void reconstitute(Long id, double latitude, double longitude, LocalDateTime recordedAt, Long recordedByUserId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.recordedAt = recordedAt;
        this.recordedByUserId = recordedByUserId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public Long getRecordedByUserId() { return recordedByUserId; }
}
