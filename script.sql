CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE toilets
(
    id          UUID  primary key,
    description TEXT,
    latitude    DECIMAL(9, 6) not null,
    longitude   DECIMAL(9, 6) not null,
    version     INTEGER not null,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
    updated_at   TIMESTAMP WITH TIME ZONE not null
);

CREATE TABLE toilet_outbox
(
    id  UUID primary key,
    content TEXT,
    type TEXT,
    published BOOLEAN,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp
);

CREATE TABLE customers
(
    id          UUID  primary key,
    name        TEXT,
    email       TEXT,
    version     INTEGER not null,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
    updated_at   TIMESTAMP WITH TIME ZONE not null
);

create table owners (
    id          UUID  primary key,
    customer_id  uuid not null,
    toilet_id uuid not null,
    version     INTEGER not null,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
    updated_at   TIMESTAMP WITH TIME ZONE not null,

    CONSTRAINT unique_customer_toilet UNIQUE (customer_id, toilet_id),
    CONSTRAINT fk_owner_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_owner_toilet FOREIGN KEY (toilet_id) REFERENCES toilets(id)
);

create table items (
    id          UUID  primary key,
    description TEXT,
    version     INTEGER not null,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
    updated_at   TIMESTAMP WITH TIME ZONE not null,
    toilet_id    uuid,
    CONSTRAINT fk_item_toilet FOREIGN KEY (toilet_id) REFERENCES toilets(id)
);

create table genres (
   id          UUID  primary key,
   description TEXT,
   created_at   TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
   updated_at   TIMESTAMP WITH TIME ZONE not null
);

INSERT INTO genres (id, description, updated_at)
VALUES (uuid_generate_v4(), 'MALE', current_timestamp);

INSERT INTO genres (id, description, updated_at)
VALUES (uuid_generate_v4(), 'FEMALE', current_timestamp);

INSERT INTO genres (id, description, updated_at)
VALUES (uuid_generate_v4(), 'UNISEX', current_timestamp);

create table toilet_genre (
    id          UUID  primary key,
    genre       TEXT not null,
    version     INTEGER not null,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
    updated_at  TIMESTAMP WITH TIME ZONE not null,
    toilet_id   UUID not null,
    genre_id    UUID not null,

    CONSTRAINT fk_toilet FOREIGN KEY (toilet_id) REFERENCES toilets(id),
    CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genres(id)
);