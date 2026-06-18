package com.opaltrace.platform.analytics.infrastructure.persistence.jpa.repositories;

import com.opaltrace.platform.analytics.infrastructure.persistence.jpa.entities.AnalyticsReportPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyticsReportPersistenceRepository extends JpaRepository<AnalyticsReportPersistenceEntity, Long> {
}
