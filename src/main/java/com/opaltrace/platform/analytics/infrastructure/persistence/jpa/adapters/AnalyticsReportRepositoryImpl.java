package com.opaltrace.platform.analytics.infrastructure.persistence.jpa.adapters;

import com.opaltrace.platform.analytics.domain.model.aggregates.AnalyticsReport;
import com.opaltrace.platform.analytics.domain.repositories.AnalyticsReportRepository;
import com.opaltrace.platform.analytics.infrastructure.persistence.jpa.assemblers.AnalyticsReportPersistenceAssembler;
import com.opaltrace.platform.analytics.infrastructure.persistence.jpa.repositories.AnalyticsReportPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AnalyticsReportRepositoryImpl implements AnalyticsReportRepository {

    private final AnalyticsReportPersistenceRepository persistenceRepository;

    public AnalyticsReportRepositoryImpl(AnalyticsReportPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public AnalyticsReport save(AnalyticsReport report) {
        var entity = AnalyticsReportPersistenceAssembler.toPersistence(report);
        var saved = persistenceRepository.save(entity);
        return AnalyticsReportPersistenceAssembler.toDomain(saved);
    }

    @Override
    public Optional<AnalyticsReport> findById(Long id) {
        return persistenceRepository.findById(id)
                .map(AnalyticsReportPersistenceAssembler::toDomain);
    }

    @Override
    public List<AnalyticsReport> findAll() {
        return persistenceRepository.findAll().stream()
                .map(AnalyticsReportPersistenceAssembler::toDomain)
                .collect(Collectors.toList());
    }
}
