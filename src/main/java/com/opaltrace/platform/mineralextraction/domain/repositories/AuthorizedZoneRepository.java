package com.opaltrace.platform.mineralextraction.domain.repositories;

import com.opaltrace.platform.mineralextraction.domain.model.aggregates.AuthorizedZone;

import java.util.List;

public interface AuthorizedZoneRepository {
    List<AuthorizedZone> findAllActive();
}
