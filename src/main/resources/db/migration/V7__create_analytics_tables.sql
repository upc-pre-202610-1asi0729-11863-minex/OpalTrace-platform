CREATE TABLE analytics_reports (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_type         VARCHAR(20) NOT NULL,
    requested_by_user_id BIGINT,
    period_start        DATE,
    period_end          DATE,
    generated_at        DATETIME NOT NULL,
    report_data         TEXT,
    compliance_status   VARCHAR(20),
    created_at          DATETIME NOT NULL,
    updated_at          DATETIME NOT NULL
);
