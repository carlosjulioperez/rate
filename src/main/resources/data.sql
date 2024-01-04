CREATE TABLE IF NOT EXISTS currency_exchange_rate (
    id INT AUTO_INCREMENT PRIMARY KEY,
    source_currency VARCHAR(3) NOT NULL,
    target_currency VARCHAR(3) NOT NULL,
    effective_start_date DATE NOT NULL,
    exchange_rate DECIMAL(10,4) NOT NULL
);
-- Example initial data
INSERT INTO currency_exchange_rate (source_currency, target_currency, effective_start_date, exchange_rate)
VALUES  ('USD', 'EUR', '2023-12-30', 0.95),
        ('USD', 'EUR', '2023-12-29', 0.93),
        ('USD', 'EUR', '2023-12-25', 0.92),
        ('USD', 'CAD', '2023-12-30', 1.33),
        ('USD', 'CHF', '2023-12-30', 0.84),
        ('EUR', 'GBP', '2023-12-30', 0.88),
        ('EUR', 'GBP', '2023-12-31', 0.91)
;
