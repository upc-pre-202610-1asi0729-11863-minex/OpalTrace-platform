package com.opaltrace.platform.jewelryinventory.domain.model.events;

import java.time.LocalDateTime;

public record CertificateDownloadedEvent(Long productId, String certificateNumber, LocalDateTime timestamp) {
}
