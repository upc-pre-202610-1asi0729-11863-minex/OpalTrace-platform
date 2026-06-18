package com.opaltrace.platform.mineralextraction.domain.model.valueobjects;

public record GpsCoordinate(double latitude, double longitude) {

    public GpsCoordinate {
        if (latitude < -90 || latitude > 90)
            throw new IllegalArgumentException("Latitude must be between -90 and 90, got: " + latitude);
        if (longitude < -180 || longitude > 180)
            throw new IllegalArgumentException("Longitude must be between -180 and 180, got: " + longitude);
    }

    /**
     * Calculates distance in meters to another coordinate using Haversine formula.
     */
    public double distanceMetersTo(GpsCoordinate other) {
        final double earthRadiusM = 6_371_000.0;
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(other.latitude);
        double deltaLat = Math.toRadians(other.latitude - this.latitude);
        double deltaLon = Math.toRadians(other.longitude - this.longitude);
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusM * c;
    }

    @Override
    public String toString() {
        return "[" + latitude + ", " + longitude + "]";
    }
}
