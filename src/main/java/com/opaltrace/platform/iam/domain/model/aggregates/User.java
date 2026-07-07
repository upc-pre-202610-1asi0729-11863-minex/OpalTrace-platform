package com.opaltrace.platform.iam.domain.model.aggregates;

import com.opaltrace.platform.iam.domain.model.commands.RegisterConsumerUserCommand;
import com.opaltrace.platform.iam.domain.model.commands.RegisterEnterpriseUserCommand;
import com.opaltrace.platform.iam.domain.model.events.UserRegisteredEvent;
import com.opaltrace.platform.iam.domain.model.valueobjects.*;
import com.opaltrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class User extends AbstractDomainAggregateRoot<User> {

    private Long id;
    private EmailAddress email;
    private HashedPassword password;
    private String fullName;
    private String companyName;
    private RucNumber ruc;
    private UserSegment segment;
    private UserRole role;
    private PlanTier planTier;
    private boolean active;
    private int failedLoginAttempts;
    private LocalDateTime lockedUntil;
    private String firstName;
    private String lastName;
    private String gender;

    public User() {}

    public void reconstitute(
            EmailAddress email,
            HashedPassword password,
            String fullName,
            String companyName,
            RucNumber ruc,
            UserSegment segment,
            UserRole role,
            PlanTier planTier,
            boolean active,
            int failedLoginAttempts,
            java.time.LocalDateTime lockedUntil,
            String firstName,
            String lastName,
            String gender) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.companyName = companyName;
        this.ruc = ruc;
        this.segment = segment;
        this.role = role;
        this.planTier = planTier;
        this.active = active;
        this.failedLoginAttempts = failedLoginAttempts;
        this.lockedUntil = lockedUntil;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public User(RegisterEnterpriseUserCommand command) {
        this.email = new EmailAddress(command.email());
        this.password = HashedPassword.fromRaw(command.password());
        this.companyName = command.companyName();
        this.ruc = new RucNumber(command.ruc());
        this.segment = command.segment();
        this.role = UserRole.SUPERVISOR;
        this.planTier = command.planTier() != null ? command.planTier() : PlanTier.GOLD;
        this.active = true;
        this.failedLoginAttempts = 0;
        this.gender = command.gender();
        registerDomainEvent(new UserRegisteredEvent(null, command.email(), command.segment()));
    }

    public User(RegisterConsumerUserCommand command) {
        this.email = new EmailAddress(command.email());
        this.password = HashedPassword.fromRaw(command.password());
        this.fullName = command.fullName();
        this.segment = UserSegment.CONSUMER;
        this.role = UserRole.CONSUMER;
        this.planTier = command.planTier() != null ? command.planTier() : PlanTier.SILVER;
        this.active = true;
        this.failedLoginAttempts = 0;
        this.gender = command.gender();
        registerDomainEvent(new UserRegisteredEvent(null, command.email(), UserSegment.CONSUMER));
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean verifyPassword(String rawPassword) {
        return this.password.matches(rawPassword);
    }

    public void recordFailedLogin() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 5) {
            this.lockedUntil = LocalDateTime.now().plusMinutes(15);
        }
    }

    public void resetFailedLogins() {
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;
    }

    public boolean isLocked() {
        return lockedUntil != null && LocalDateTime.now().isBefore(lockedUntil);
    }

    public void changePassword(HashedPassword newPassword) {
        this.password = newPassword;
    }

    public void upgradePlan(PlanTier newTier) {
        this.planTier = newTier;
    }

    public void updateProfile(EmailAddress email, String firstName, String lastName, String gender) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fullName = "%s %s".formatted(firstName, lastName).trim();
    }
}
