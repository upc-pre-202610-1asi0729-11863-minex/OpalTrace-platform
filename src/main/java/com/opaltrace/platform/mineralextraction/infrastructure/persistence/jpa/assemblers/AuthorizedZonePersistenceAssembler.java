package com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.assemblers;

import com.opaltrace.platform.mineralextraction.domain.model.aggregates.AuthorizedZone;
import com.opaltrace.platform.mineralextraction.domain.model.valueobjects.GpsCoordinate;
import com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.entities.AuthorizedZonePersistenceEntity;

public final class AuthorizedZonePersistenceAssembler {

    private AuthorizedZonePersistenceAssembler() {}

    public static AuthorizedZone toDomainFromPersistence(AuthorizedZonePersistenceEntity entity) {
        if (entity == null) return null;
        var zone = new AuthorizedZone();
        zone.setId(entity.getId());
        zone.reconstitute(
                entity.getZoneName(),
                new GpsCoordinate(entity.getCenterLatitude(), entity.getCenterLongitude()),
                entity.getRadiusMeters(),
                entity.isActive()
        );
        return zone;
    }
}
