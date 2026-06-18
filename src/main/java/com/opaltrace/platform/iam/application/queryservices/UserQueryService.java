package com.opaltrace.platform.iam.application.queryservices;

import com.opaltrace.platform.iam.domain.model.aggregates.User;
import com.opaltrace.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.opaltrace.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.opaltrace.platform.iam.domain.model.queries.GetUserByIdQuery;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> handle(GetUserByEmailQuery query);
    List<User> handle(GetAllUsersQuery query);
}
