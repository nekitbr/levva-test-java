CREATE TABLE order_products
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    price      INTEGER      NOT NULL,
    quantity   INTEGER      NOT NULL,
    order_id   INTEGER      NOT NULL REFERENCES orders (id) ON DELETE CASCADE,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- Audit column for creation timestamp
    created_by VARCHAR(255),                                       -- Audit column for creator
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- Audit column for last update timestamp
    updated_by VARCHAR(255)                                        -- Audit column for last updater
);

CREATE INDEX idx_order_products_order ON order_products (order_id);
