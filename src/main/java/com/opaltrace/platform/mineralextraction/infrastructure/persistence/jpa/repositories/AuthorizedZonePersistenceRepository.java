package com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.repositories;

import com.opaltrace.platform.mineralextraction.infrastructure.persistence.jpa.entities.AuthorizedZonePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorizedZonePersistenceRepository extends JpaRepository<AuthorizedZonePersistenceEntity, Long> {
    List<AuthorizedZonePersistenceEntity> findByActiveTrue();
}
