package com.opaltrace.platform.analytics.infrastructure.persistence.jpa.entities;

import com.opaltrace.platform.analytics.domain.model.valueobjects.ComplianceStatus;
import com.opaltrace.platform.analytics.domain.model.valueobjects.ReportType;
import com.opaltrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "analytics_reports")
@Getter
@Setter
@NoArgsConstructor
public class AnalyticsReportPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false, length = 20)
    private ReportType reportType;

    @Column(name = "requested_by_user_id")
    private Long requestedByUserId;

    @Column(name = "period_start")
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "report_data", columnDefinition = "TEXT")
    private String reportData;

    @Enumerated(EnumType.STRING)
    @Column(name = "compliance_status", length = 20)
    private ComplianceStatus complianceStatus;
}
