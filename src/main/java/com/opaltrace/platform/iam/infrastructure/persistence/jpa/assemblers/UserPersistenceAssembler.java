package com.opaltrace.platform.iam.infrastructure.persistence.jpa.assemblers;

import com.opaltrace.platform.iam.domain.model.aggregates.User;
import com.opaltrace.platform.iam.domain.model.valueobjects.EmailAddress;
import com.opaltrace.platform.iam.domain.model.valueobjects.HashedPassword;
import com.opaltrace.platform.iam.domain.model.valueobjects.RucNumber;
import com.opaltrace.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;

public final class UserPersistenceAssembler {

    private UserPersistenceAssembler() {}

    public static User toDomainFromPersistence(UserPersistenceEntity entity) {
        if (entity == null) return null;
        var user = new User();
        user.setId(entity.getId());
        user.reconstitute(
                new EmailAddress(entity.getEmail()),
                HashedPassword.fromHash(entity.getPasswordHash()),
                entity.getFullName(),
                entity.getCompanyName(),
                entity.getRuc() != null ? new RucNumber(entity.getRuc()) : null,
                entity.getSegment(),
                entity.getRole(),
                entity.getPlanTier(),
                entity.isActive(),
                entity.getFailedLoginAttempts(),
                entity.getLockedUntil()
        );
        return user;
    }

    public static UserPersistenceEntity toPersistenceFromDomain(User user) {
        if (user == null) return null;
        var entity = new UserPersistenceEntity();
        if (user.getId() != null) entity.setId(user.getId());
        entity.setEmail(user.getEmail().value());
        entity.setPasswordHash(user.getPassword().value());
        entity.setFullName(user.getFullName());
        entity.setCompanyName(user.getCompanyName());
        entity.setRuc(user.getRuc() != null ? user.getRuc().value() : null);
        entity.setSegment(user.getSegment());
        entity.setRole(user.getRole());
        entity.setPlanTier(user.getPlanTier());
        entity.setActive(user.isActive());
        entity.setFailedLoginAttempts(user.getFailedLoginAttempts());
        entity.setLockedUntil(user.getLockedUntil());
        return entity;
    }
}
