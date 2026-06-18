package com.opaltrace.platform.jewelryinventory.interfaces.rest.resources;

import java.time.LocalDateTime;

public record CertificateResource(
        Long productId,
        String certificateNumber,
        String productName,
        String batchId,
        Long jewelryId,
        LocalDateTime certifiedAt,
        double weightGrams,
        String verificationUrl
) {
}
