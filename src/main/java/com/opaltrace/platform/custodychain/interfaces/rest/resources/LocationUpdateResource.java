package com.opaltrace.platform.custodychain.interfaces.rest.resources;

import java.time.LocalDateTime;

public record LocationUpdateResource(
        Long id,
        double latitude,
        double longitude,
        LocalDateTime recordedAt,
        Long recordedByUserId
) {}
