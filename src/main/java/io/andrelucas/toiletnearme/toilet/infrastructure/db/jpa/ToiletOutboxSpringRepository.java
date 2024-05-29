package io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ToiletOutboxSpringRepository extends CrudRepository<ToiletOutboxEntity, UUID> {
    List<ToiletOutboxEntity> findAllByPublishedFalse();
}
