CREATE TABLE IF NOT EXISTS users (
    id                    BIGINT          NOT NULL AUTO_INCREMENT,
    email                 VARCHAR(255)    NOT NULL UNIQUE,
    password_hash         VARCHAR(255)    NOT NULL,
    full_name             VARCHAR(255),
    company_name          VARCHAR(255),
    ruc                   VARCHAR(11),
    segment               VARCHAR(20)     NOT NULL,
    role                  VARCHAR(20)     NOT NULL,
    plan_tier             VARCHAR(20)     NOT NULL,
    active                BOOLEAN         NOT NULL DEFAULT TRUE,
    failed_login_attempts INT             NOT NULL DEFAULT 0,
    locked_until          DATETIME,
    created_at            DATETIME        NOT NULL,
    updated_at            DATETIME        NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
