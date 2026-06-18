package com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.adapters;

import com.opaltrace.platform.mineralextraction.domain.model.aggregates.AuthorizedZone;
import com.opaltrace.platform.mineralextraction.domain.repositories.AuthorizedZoneRepository;
import com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.assemblers.AuthorizedZonePersistenceAssembler;
import com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.repositories.AuthorizedZonePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorizedZoneRepositoryImpl implements AuthorizedZoneRepository {

    private final AuthorizedZonePersistenceRepository authorizedZonePersistenceRepository;

    public AuthorizedZoneRepositoryImpl(AuthorizedZonePersistenceRepository authorizedZonePersistenceRepository) {
        this.authorizedZonePersistenceRepository = authorizedZonePersistenceRepository;
    }

    @Override
    public List<AuthorizedZone> findAllActive() {
        return authorizedZonePersistenceRepository.findByActiveTrue().stream()
                .map(AuthorizedZonePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
