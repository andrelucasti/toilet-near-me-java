package io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.andrelucas.toiletnearme.toilet.business.Toilet;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEvent;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Repository
public class ToiletRepositoryJPAImpl implements ToiletRepository {
    private final ToiletSpringRepository toiletSpringRepository;
    private final ToiletOutboxSpringRepository toiletOutboxSpringRepository;
    private final ObjectMapper objectMapper;

    public ToiletRepositoryJPAImpl(final ToiletSpringRepository toiletSpringRepository,
                                   final ToiletOutboxSpringRepository toiletOutboxSpringRepository,
                                   final ObjectMapper objectMapper) {
        this.toiletSpringRepository = toiletSpringRepository;
        this.toiletOutboxSpringRepository = toiletOutboxSpringRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void save(final Toilet toilet) {
        final var toiletEntity = ToiletEntity.from(toilet);
        toiletSpringRepository.save(toiletEntity);

        final var events = toilet.domainEvents()
                .stream()
                .map(it -> ToiletOutboxEntity.of(it, this.toJson(it)))
                .toList();

        toiletOutboxSpringRepository.saveAll(events);
    }

    @Override
    public Optional<Toilet> findById(final ToiletId toiletId) {
        return toiletSpringRepository.findById(UUID.fromString(toiletId.value()))
                .map(ToiletEntity::toToilet);
    }

    @Override
    public List<Toilet> findAll() {
        return StreamSupport.stream(toiletSpringRepository.findAll().spliterator(), false)
                .map(ToiletEntity::toToilet)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Toilet entity) {

    }

    @Override
    @Transactional
    public void update(final Toilet entity) {
        final var toiletEntity = ToiletEntity.from(entity);
        toiletSpringRepository.save(toiletEntity);

        final var events = entity.domainEvents()
                .stream()
                .map(it -> ToiletOutboxEntity.of(it, this.toJson(it)))
                .toList();

        toiletOutboxSpringRepository.saveAll(events);
    }

    private String toJson(final ToiletEvent toiletEvent) {
        try {
            return objectMapper.writeValueAsString(toiletEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
