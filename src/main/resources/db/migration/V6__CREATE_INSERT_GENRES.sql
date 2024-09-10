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