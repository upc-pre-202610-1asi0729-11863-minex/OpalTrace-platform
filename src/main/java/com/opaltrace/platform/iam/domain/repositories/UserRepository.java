package com.opaltrace.platform.iam.domain.repositories;

import com.opaltrace.platform.iam.domain.model.aggregates.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    boolean existsByEmail(String email);
    void deleteById(Long id);
}
