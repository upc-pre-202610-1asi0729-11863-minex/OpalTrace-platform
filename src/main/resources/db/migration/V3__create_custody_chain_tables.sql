CREATE TABLE custody_transfers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_id VARCHAR(20),
    batch_pk BIGINT,
    custody_holder_user_id BIGINT,
    status VARCHAR(20),
    start_latitude DOUBLE,
    start_longitude DOUBLE,
    end_latitude DOUBLE,
    end_longitude DOUBLE,
    blockchain_tx_hash VARCHAR(100),
    started_at DATETIME,
    completed_at DATETIME,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE location_updates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transfer_id BIGINT NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    recorded_at DATETIME,
    recorded_by_user_id BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    CONSTRAINT fk_location_updates_transfer FOREIGN KEY (transfer_id) REFERENCES custody_transfers(id)
);
