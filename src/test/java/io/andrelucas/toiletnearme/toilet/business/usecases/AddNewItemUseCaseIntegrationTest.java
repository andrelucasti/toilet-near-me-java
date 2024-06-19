package io.andrelucas.toiletnearme.toilet.business.usecases;

import io.andrelucas.toiletnearme.AbstractIntegrationTest;
import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.toilet.business.Geolocation;
import io.andrelucas.toiletnearme.toilet.business.Toilet;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletCreatedEvent;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEventType;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.StreamSupport;

class AddNewItemUseCaseIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldSaveItemWhenToiletIsUpdated() {
        final var toiletId = ToiletId.newId();
        final var toilet = new Toilet(toiletId, "Toilet", new Geolocation(0.0, 0.0), 0L,
                Set.of(), Set.of(new ToiletCreatedEvent(toiletId, CustomerId.newId()))) ;
        toiletRepository.save(toilet);

        final var input = new AddNewItemUseCase.Input("Soap", toilet.id().value());
        final var subject = new AddNewItemUseCase();

        subject.execute(toiletRepository::findById,
                        toiletRepository::update)
                .apply(input);

        final var updatedToilet = toiletRepository.findById(toilet.id()).get();

        Assertions.assertThat(updatedToilet.items()).hasSize(1);
        Assertions.assertThat(updatedToilet.items()).extracting("description").containsExactly("Soap");
    }

    @Test
    void shouldRegisterEventInTheOutboxingTableWhenTheToiletIsUpdated() {
        final var toilet = new Toilet(ToiletId.newId(), "Toilet", new Geolocation(0.0, 0.0), 0, Set.of());
        toiletRepository.save(toilet);

        final var input = new AddNewItemUseCase.Input("Soap", toilet.id().value());
        final var subject = new AddNewItemUseCase();

        subject.execute(toiletRepository::findById, toiletRepository::update)
                .apply(input);

        final var events = StreamSupport.stream(toiletOutboxSpringRepository.findAll().spliterator(), false)
                .map(ToiletOutboxEntity::getType)
                .toList();

        Assertions.assertThat(events).contains(ToiletEventType.ItemCreatedEvent);
    }
}