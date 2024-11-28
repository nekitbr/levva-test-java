CREATE TABLE users
(
    id               SERIAL PRIMARY KEY,
    uid              VARCHAR(28)  NOT NULL UNIQUE,
    username         VARCHAR(100) NOT NULL UNIQUE,
    email            VARCHAR(255) NOT NULL UNIQUE,
    role             VARCHAR(12)  NOT NULL,
    cellphone_number VARCHAR(15),
    avatar_url       VARCHAR(255),
    disabled         BOOLEAN                  DEFAULT false,
    last_login       TIMESTAMP WITH TIME ZONE,

    created_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- Audit column for creation timestamp
    created_by       VARCHAR(255),                                       -- Audit column for creator
    updated_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- Audit column for last update timestamp
    updated_by       VARCHAR(255)--,                                     -- Audit column for last updater
);

-- create index on ID if it doesn't exist already
DO
$$
BEGIN
        -- Check if an index on the 'id' column exists
        IF
NOT EXISTS (SELECT 1
                       FROM pg_indexes
                       WHERE tablename = 'users'
                         AND indexdef LIKE '%(id)%') THEN
            -- Create the index if it does not exist
            EXECUTE 'CREATE INDEX idx_users_id ON users (id)';
END IF;
END
$$;

CREATE INDEX idx_users_uid ON users (uid);
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_users_cellphone_number ON users (cellphone_number);
