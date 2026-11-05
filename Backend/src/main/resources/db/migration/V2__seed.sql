INSERT IGNORE INTO ascend_api_usage
    (id, monthly_count, hourly_count, monthly_period_start, hourly_period_start)
VALUES
    (1, 0, 0, '2000-01-01', '2000-01-01 00:00:00');

INSERT INTO users_seq (next_val) SELECT 1 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM users_seq);
