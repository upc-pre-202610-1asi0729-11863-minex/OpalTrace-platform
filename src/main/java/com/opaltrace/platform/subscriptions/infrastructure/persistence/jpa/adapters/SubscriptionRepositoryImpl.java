package com.opaltrace.platform.subscriptions.infrastructure.persistence.jpa.adapters;

import com.opaltrace.platform.subscriptions.domain.model.aggregates.Subscription;
import com.opaltrace.platform.subscriptions.domain.repositories.SubscriptionRepository;
import com.opaltrace.platform.subscriptions.infrastructure.persistence.jpa.assemblers.SubscriptionPersistenceAssembler;
import com.opaltrace.platform.subscriptions.infrastructure.persistence.jpa.repositories.SubscriptionPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final SubscriptionPersistenceRepository persistenceRepository;

    public SubscriptionRepositoryImpl(SubscriptionPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        var entity = SubscriptionPersistenceAssembler.toPersistence(subscription);
        var saved = persistenceRepository.save(entity);
        return SubscriptionPersistenceAssembler.toDomain(saved);
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return persistenceRepository.findById(id)
                .map(SubscriptionPersistenceAssembler::toDomain);
    }

    @Override
    public Optional<Subscription> findByUserId(Long userId) {
        return persistenceRepository.findByUserId(userId)
                .map(SubscriptionPersistenceAssembler::toDomain);
    }

    @Override
    public List<Subscription> findAll() {
        return persistenceRepository.findAll().stream()
                .map(SubscriptionPersistenceAssembler::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return persistenceRepository.existsByUserId(userId);
    }
}
