ALTER TABLE product_verifications
    ADD COLUMN consumer_id BIGINT NULL;

CREATE INDEX idx_pv_consumer ON product_verifications (consumer_id);
