package com.opaltrace.platform.subscriptions.infrastructure.persistence.jpa.repositories;

import com.opaltrace.platform.subscriptions.infrastructure.persistence.jpa.entities.SubscriptionPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionPersistenceRepository extends JpaRepository<SubscriptionPersistenceEntity, Long> {
    Optional<SubscriptionPersistenceEntity> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
