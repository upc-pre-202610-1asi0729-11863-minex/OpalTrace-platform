package com.opaltrace.platform.iam.application.internal.commandservices;

import com.opaltrace.platform.iam.application.commandservices.UserCommandService;
import com.opaltrace.platform.iam.domain.model.aggregates.User;
import com.opaltrace.platform.iam.domain.model.commands.*;
import com.opaltrace.platform.iam.domain.model.valueobjects.HashedPassword;
import com.opaltrace.platform.iam.domain.repositories.UserRepository;
import com.opaltrace.platform.shared.application.result.ApplicationError;
import com.opaltrace.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;

    public UserCommandServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Result<Long, ApplicationError> handle(RegisterEnterpriseUserCommand command) {
        if (userRepository.existsByEmail(command.email()))
            return Result.failure(ApplicationError.conflict("User", "Email '%s' is already registered".formatted(command.email())));
        try {
            var user = new User(command);
            user = userRepository.save(user);
            return Result.success(user.getId());
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("register-enterprise-user", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("register-enterprise-user", e.getMessage()));
        }
    }

    @Override
    public Result<Long, ApplicationError> handle(RegisterConsumerUserCommand command) {
        if (userRepository.existsByEmail(command.email()))
            return Result.failure(ApplicationError.conflict("User", "Email '%s' is already registered".formatted(command.email())));
        try {
            var user = new User(command);
            user = userRepository.save(user);
            return Result.success(user.getId());
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("register-consumer-user", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("register-consumer-user", e.getMessage()));
        }
    }

    @Override
    public Result<User, ApplicationError> handle(SignInCommand command) {
        var userOpt = userRepository.findByEmail(command.email());
        if (userOpt.isEmpty())
            return Result.failure(ApplicationError.unauthorized("sign-in", "Invalid email or password"));

        var user = userOpt.get();

        if (user.isLocked())
            return Result.failure(ApplicationError.unauthorized("sign-in", "Account is temporarily locked. Please try again in 15 minutes"));

        if (!user.verifyPassword(command.password())) {
            user.recordFailedLogin();
            userRepository.save(user);
            return Result.failure(ApplicationError.unauthorized("sign-in", "Invalid email or password"));
        }

        user.resetFailedLogins();
        userRepository.save(user);
        return Result.success(user);
    }

    @Override
    public Result<Long, ApplicationError> handle(ChangePasswordCommand command) {
        var userOpt = userRepository.findById(command.userId());
        if (userOpt.isEmpty())
            return Result.failure(ApplicationError.notFound("User", command.userId().toString()));

        var user = userOpt.get();

        if (!user.verifyPassword(command.currentPassword()))
            return Result.failure(ApplicationError.unauthorized("change-password", "Current password is incorrect"));

        try {
            user.changePassword(HashedPassword.fromRaw(command.newPassword()));
            userRepository.save(user);
            return Result.success(user.getId());
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("change-password", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("change-password", e.getMessage()));
        }
    }
}
