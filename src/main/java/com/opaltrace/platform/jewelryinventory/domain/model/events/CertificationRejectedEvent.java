package com.opaltrace.platform.jewelryinventory.domain.model.events;

import java.time.LocalDateTime;

public record CertificationRejectedEvent(Long productId, String reason, LocalDateTime timestamp) {
}
