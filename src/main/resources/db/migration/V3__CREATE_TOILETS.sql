CREATE TABLE toilets
(
    id          UUID  primary key,
    description TEXT,
    latitude    DECIMAL(9, 6) not null,
    longitude   DECIMAL(9, 6) not null,
    type        TEXT not null,
    price       BIGINT default 0,
    version     INTEGER not null,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
    updated_at   TIMESTAMP WITH TIME ZONE not null
);