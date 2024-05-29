package io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ToiletSpringRepository extends CrudRepository<ToiletEntity, UUID> {

}
