CREATE TABLE jewelry_products
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code            VARCHAR(50)  NOT NULL,
    name                    VARCHAR(255) NOT NULL,
    description             TEXT,
    jewelry_id              BIGINT,
    category                VARCHAR(30)  NOT NULL,
    batch_id                VARCHAR(20),
    batch_pk                BIGINT,
    external_supplier_name  VARCHAR(255),
    is_certified_source     BOOLEAN      NOT NULL DEFAULT FALSE,
    can_generate_certificate BOOLEAN     NOT NULL DEFAULT FALSE,
    certification_state     VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    certificate_number      VARCHAR(30) UNIQUE,
    blockchain_tx_hash      VARCHAR(100),
    certified_at            DATETIME,
    weight_grams            DOUBLE       NOT NULL,
    created_at              DATETIME     NOT NULL,
    updated_at              DATETIME     NOT NULL
);
