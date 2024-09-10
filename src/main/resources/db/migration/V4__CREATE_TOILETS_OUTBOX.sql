CREATE TABLE toilet_outbox
(
    id  UUID primary key,
    content TEXT,
    type TEXT,
    published BOOLEAN,
    received BOOLEAN,
    version INTEGER not null,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT current_timestamp,
    updated_at TIMESTAMP WITH TIME ZONE not null
);
