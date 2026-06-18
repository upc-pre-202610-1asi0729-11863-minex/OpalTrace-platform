CREATE TABLE refinery_receipts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_pk BIGINT,
    batch_id VARCHAR(20),
    refinery_id BIGINT,
    supervisor_id BIGINT,
    declared_weight_kg DOUBLE,
    original_weight_kg DOUBLE,
    weight_discrepancy_percent DOUBLE,
    received_at DATETIME,
    processing_completed_at DATETIME,
    blockchain_tx_hash VARCHAR(100),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE sub_lot_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_id BIGINT NOT NULL,
    parent_batch_pk BIGINT,
    child_batch_pk BIGINT,
    child_batch_id VARCHAR(20),
    weight_kg DOUBLE,
    created_at_record DATETIME,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT fk_sub_lot_records_receipt FOREIGN KEY (receipt_id) REFERENCES refinery_receipts(id)
);
