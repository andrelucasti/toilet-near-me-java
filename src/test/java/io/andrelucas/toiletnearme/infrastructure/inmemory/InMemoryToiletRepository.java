package io.andrelucas.toiletnearme.infrastructure.inmemory;

import io.andrelucas.toiletnearme.toilet.business.Toilet;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import io.andrelucas.toiletnearme.toilet.business.ToiletRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryToiletRepository implements ToiletRepository {
    private final Map<ToiletId, Toilet> toilets = new HashMap<>();

    @Override
    public void delete(Toilet entity) {
        toilets.remove(entity.id());
    }

    @Override
    public List<Toilet> findAll() {
        return List.copyOf(toilets.values());
    }

    @Override
    public Optional<Toilet> findById(ToiletId toiletId) {
        return Optional.ofNullable(toilets.get(toiletId));
    }

    @Override
    public void save(Toilet entity) {
        toilets.put(entity.id(), entity);
    }

    @Override
    public void update(Toilet entity) {
        toilets.replace(entity.id(), entity);
    }
}
