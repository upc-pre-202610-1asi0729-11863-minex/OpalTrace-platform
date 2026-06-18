package com.opaltrace.platform.jewelryinventory.domain.model.events;

import java.time.LocalDateTime;

public record MaterialReceivedEvent(String batchId, Long productId, Long jewelryId, LocalDateTime timestamp) {
}
