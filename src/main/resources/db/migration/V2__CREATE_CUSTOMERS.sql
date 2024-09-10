CREATE TABLE customers
(
    id          UUID  primary key,
    name        TEXT,
    email       TEXT,
    version     INTEGER not null,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
    updated_at   TIMESTAMP WITH TIME ZONE not null
);