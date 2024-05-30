package io.andrelucas.toiletnearme.owner.infrastructure.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface OwnerSpringRepository extends CrudRepository<OwnerEntity, UUID>{

    Optional<OwnerEntity> findByToiletId(UUID toiletId);
}
