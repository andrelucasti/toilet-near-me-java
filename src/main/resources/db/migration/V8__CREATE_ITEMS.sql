create table items (
    id          UUID  primary key,
    description TEXT,
    version     INTEGER not null,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
    updated_at   TIMESTAMP WITH TIME ZONE not null,
    toilet_id    uuid,
    CONSTRAINT fk_item_toilet FOREIGN KEY (toilet_id) REFERENCES toilets(id)
);
