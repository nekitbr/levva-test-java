CREATE TABLE orders
(
    id          SERIAL PRIMARY KEY,
    fiscal_code VARCHAR(255) NOT NULL UNIQUE,
    status      VARCHAR(30)  NOT NULL,
    price       INTEGER      NOT NULL,
    currency    VARCHAR(3)   NOT NULL,
    customer_id INTEGER      NOT NULL,

    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- Audit column for creation timestamp
    created_by  VARCHAR(255),                                       -- Audit column for creator
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- Audit column for last update timestamp
    updated_by  VARCHAR(255)                                        -- Audit column for last updater
);

CREATE INDEX idx_orders_customer ON orders (customer_id);
