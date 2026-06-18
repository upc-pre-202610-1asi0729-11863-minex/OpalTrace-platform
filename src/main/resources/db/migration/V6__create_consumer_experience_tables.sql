CREATE TABLE product_verifications
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    certificate_id      VARCHAR(50)  NOT NULL,
    verification_result VARCHAR(20)  NOT NULL,
    failure_reason      VARCHAR(500),
    verifier_ip         VARCHAR(50),
    verified_at         DATETIME     NOT NULL,
    product_id          BIGINT,
    batch_id            VARCHAR(20),
    created_at          DATETIME     NOT NULL,
    updated_at          DATETIME     NOT NULL,
    INDEX idx_pv_certificate (certificate_id)
);
