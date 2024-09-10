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
