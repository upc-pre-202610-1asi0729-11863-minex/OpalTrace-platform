package com.opaltrace.platform.iam.infrastructure.persistence.jpa.repositories;

import com.opaltrace.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPersistenceRepository extends JpaRepository<UserPersistenceEntity, Long> {
    Optional<UserPersistenceEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
