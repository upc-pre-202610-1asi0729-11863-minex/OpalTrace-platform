package com.opaltrace.platform.subscriptions.domain.repositories;

import com.opaltrace.platform.subscriptions.domain.model.aggregates.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);
    Optional<Subscription> findById(Long id);
    Optional<Subscription> findByUserId(Long userId);
    List<Subscription> findAll();
    boolean existsByUserId(Long userId);
}
