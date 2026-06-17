package com.opaltrace.platform.iam.domain.model.events;

import com.opaltrace.platform.iam.domain.model.valueobjects.UserSegment;

public record UserRegisteredEvent(
        Long userId,
        String email,
        UserSegment segment
) {}
