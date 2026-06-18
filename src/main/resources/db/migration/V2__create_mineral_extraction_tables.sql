CREATE TABLE IF NOT EXISTS authorized_zones (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    zone_name        VARCHAR(255) NOT NULL,
    center_latitude  DOUBLE       NOT NULL,
    center_longitude DOUBLE       NOT NULL,
    radius_meters    DOUBLE       NOT NULL DEFAULT 500.0,
    active           BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at       DATETIME     NOT NULL,
    updated_at       DATETIME     NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS mineral_batches (
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    batch_id           VARCHAR(20)  NOT NULL UNIQUE,
    mineral_type       VARCHAR(20)  NOT NULL,
    weight_kg          DOUBLE       NOT NULL,
    origin_latitude    DOUBLE       NOT NULL,
    origin_longitude   DOUBLE       NOT NULL,
    status             VARCHAR(20)  NOT NULL DEFAULT 'EN_ORIGEN',
    blocked            BOOLEAN      NOT NULL DEFAULT FALSE,
    supervisor_id      BIGINT,
    mining_company_id  BIGINT,
    blockchain_tx_hash VARCHAR(100),
    parent_batch_id    BIGINT,
    qr_code_data       VARCHAR(500),
    created_at         DATETIME     NOT NULL,
    updated_at         DATETIME     NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_mineral_batches_batch_id (batch_id),
    INDEX idx_mineral_batches_status (status),
    INDEX idx_mineral_batches_company (mining_company_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS anomaly_reports (
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    batch_id            BIGINT       NOT NULL,
    description         TEXT         NOT NULL,
    category            VARCHAR(30)  NOT NULL,
    photo_evidence_url  VARCHAR(500),
    resolved            BOOLEAN      NOT NULL DEFAULT FALSE,
    reported_at         DATETIME     NOT NULL,
    reported_by_user_id BIGINT,
    created_at          DATETIME     NOT NULL,
    updated_at          DATETIME     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_anomaly_batch FOREIGN KEY (batch_id) REFERENCES mineral_batches(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seed: Authorized mining zones (Peru example coordinates)
INSERT INTO authorized_zones (zone_name, center_latitude, center_longitude, radius_meters, active, created_at, updated_at) VALUES
('Zona Minera Cajamarca Norte',   -7.1583,  -78.5127, 500.0, TRUE, NOW(), NOW()),
('Zona Minera Arequipa Sur',     -16.4090,  -71.5375, 500.0, TRUE, NOW(), NOW()),
('Zona Minera Cusco Este',       -13.5319,  -71.9675, 500.0, TRUE, NOW(), NOW()),
('Zona Minera Puno Altiplano',   -15.8402,  -70.0219, 500.0, TRUE, NOW(), NOW()),
('Zona Minera Madre de Dios',    -12.5931,  -69.1891, 500.0, TRUE, NOW(), NOW());
