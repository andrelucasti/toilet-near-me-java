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