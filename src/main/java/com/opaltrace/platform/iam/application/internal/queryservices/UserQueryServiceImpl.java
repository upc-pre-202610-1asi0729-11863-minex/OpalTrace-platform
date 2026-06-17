package com.opaltrace.platform.iam.application.internal.queryservices;

import com.opaltrace.platform.iam.application.queryservices.UserQueryService;
import com.opaltrace.platform.iam.domain.model.aggregates.User;
import com.opaltrace.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.opaltrace.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.opaltrace.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.opaltrace.platform.iam.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail(query.email());
    }

    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }
}
