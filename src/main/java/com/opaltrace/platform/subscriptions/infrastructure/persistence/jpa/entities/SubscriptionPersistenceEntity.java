package com.opaltrace.platform.subscriptions.infrastructure.persistence.jpa.entities;

import com.opaltrace.platform.iam.domain.model.valueobjects.PlanTier;
import com.opaltrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.BillingCycle;
import com.opaltrace.platform.subscriptions.domain.model.valueobjects.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
public class SubscriptionPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_tier", nullable = false, length = 20)
    private PlanTier planTier;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SubscriptionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false, length = 20)
    private BillingCycle billingCycle;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "next_billing_date")
    private LocalDate nextBillingDate;

    @Column(name = "pending_downgrade", nullable = false)
    private boolean pendingDowngrade = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_downgrade_tier", length = 20)
    private PlanTier targetDowngradeTier;

    @Column(name = "downgrade_effective_date")
    private LocalDate downgradeEffectiveDate;

    @Column(name = "cancel_effective_date")
    private LocalDate cancelEffectiveDate;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillingRecordPersistenceEntity> billingRecords = new ArrayList<>();
}
