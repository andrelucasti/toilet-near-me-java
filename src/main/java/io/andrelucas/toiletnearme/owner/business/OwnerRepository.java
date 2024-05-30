package io.andrelucas.toiletnearme.owner.business;

import io.andrelucas.toiletnearme.common.Repository;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;

import java.util.Optional;

public interface OwnerRepository extends Repository<Owner, OwnerId> {
    Optional<Owner> findByToiletId(ToiletId toiletId);
}
