package io.andrelucas.toiletnearme.owner.infrastructure.jpa;

import io.andrelucas.toiletnearme.owner.business.Owner;
import io.andrelucas.toiletnearme.owner.business.OwnerId;
import io.andrelucas.toiletnearme.owner.business.OwnerRepository;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Repository
public class OwnerJPARepositoryImpl implements OwnerRepository {
    private final OwnerSpringRepository ownerSpringRepository;

    public OwnerJPARepositoryImpl(final OwnerSpringRepository ownerSpringRepository) {
        this.ownerSpringRepository = ownerSpringRepository;
    }

    @Override
    public void save(final Owner entity) {
        ownerSpringRepository.save(OwnerEntity.from(entity));
    }

    @Override
    public Optional<Owner> findById(final OwnerId ownerId) {
        return ownerSpringRepository.findById(UUID.fromString(ownerId.value())).map(OwnerEntity::toOwner);
    }

    @Override
    public List<Owner> findAll() {
        return StreamSupport
                .stream(ownerSpringRepository.findAll().spliterator(), false)
                .map(OwnerEntity::toOwner)
                .toList();
    }

    @Override
    public void delete(Owner entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void update(Owner entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Optional<Owner> findByToiletId(final ToiletId toiletId) {
        return ownerSpringRepository.findByToiletId(UUID.fromString(toiletId.value()))
                .map(OwnerEntity::toOwner);
    }
}
