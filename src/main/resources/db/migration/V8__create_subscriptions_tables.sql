CREATE TABLE subscriptions (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id                 BIGINT NOT NULL UNIQUE,
    plan_tier               VARCHAR(20) NOT NULL,
    status                  VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    billing_cycle           VARCHAR(20) NOT NULL DEFAULT 'MONTHLY',
    start_date              DATE,
    end_date                DATE,
    next_billing_date       DATE,
    pending_downgrade       BOOLEAN NOT NULL DEFAULT FALSE,
    target_downgrade_tier   VARCHAR(20),
    downgrade_effective_date DATE,
    cancel_effective_date   DATE,
    created_at              DATETIME NOT NULL,
    updated_at              DATETIME NOT NULL
);

CREATE TABLE billing_records (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    subscription_id  BIGINT NOT NULL,
    invoice_number   VARCHAR(30) NOT NULL UNIQUE,
    amount           DOUBLE NOT NULL,
    plan_tier        VARCHAR(20) NOT NULL,
    payment_status   VARCHAR(20) NOT NULL,
    payment_method   VARCHAR(20),
    transaction_date DATETIME NOT NULL,
    created_at       DATETIME NOT NULL,
    updated_at       DATETIME NOT NULL,
    CONSTRAINT fk_billing_subscription FOREIGN KEY (subscription_id) REFERENCES subscriptions(id),
    INDEX idx_billing_subscription (subscription_id)
);
