package com.opaltrace.platform.mineralextraction.domain.model.aggregates;

import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.GpsCoordinate;
import com.opaltrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

@Getter
public class AuthorizedZone extends AbstractDomainAggregateRoot<AuthorizedZone> {

    private Long id;
    private String zoneName;
    private GpsCoordinate center;
    private double radiusMeters;
    private boolean active;

    public AuthorizedZone() {}

    public void setId(Long id) { this.id = id; }

    public void reconstitute(String zoneName, GpsCoordinate center, double radiusMeters, boolean active) {
        this.zoneName = zoneName;
        this.center = center;
        this.radiusMeters = radiusMeters;
        this.active = active;
    }

    public boolean containsCoordinate(GpsCoordinate coord) {
        return active && center.distanceMetersTo(coord) <= radiusMeters;
    }
}
