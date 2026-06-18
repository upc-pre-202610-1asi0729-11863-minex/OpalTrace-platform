package com.opaltrace.platform.jewelryinventory.domain.model.events;

import java.time.LocalDateTime;

public record CertificationGrantedEvent(Long productId, String certificateNumber, Long jewelryId, String batchId, LocalDateTime timestamp) {
}
