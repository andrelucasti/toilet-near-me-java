package io.andrelucas.toiletnearme.toilet.infrastructure.events;

import io.andrelucas.toiletnearme.customer.CustomerId;
import io.andrelucas.toiletnearme.owner.business.commands.CreateOwnerCommand;
import io.andrelucas.toiletnearme.owner.business.commands.OwnerCommandPublisher;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletCreatedEvent;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxEntity;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.*;

class ToiletCreatedEventListenerTest {

    private OwnerCommandPublisher ownerCommandPublisher;
    private ToiletOutboxSpringRepository toiletOutboxSpringRepository;

    private ToiletCreatedEventListener subject;

    @BeforeEach
    void setUp() {
        ownerCommandPublisher = mock(OwnerCommandPublisher.class);
        toiletOutboxSpringRepository = mock(ToiletOutboxSpringRepository.class);

        subject = new ToiletCreatedEventListener(toiletOutboxSpringRepository, ownerCommandPublisher);
    }

    @Test
    void shouldUpdateOutboxWhenReceiveAnEvent() {
        final var captor = ArgumentCaptor.forClass(ToiletOutboxEntity.class);
        final var event = new ToiletCreatedEvent(ToiletId.newId(), CustomerId.newId());
        final var toiletOutboxEntity = new ToiletOutboxEntity(
                event.idempotentId(),
                event.type(),
                "{}",
                false,
                event.creationDate()
        );

        when(toiletOutboxSpringRepository.findById(event.idempotentId()))
                .thenReturn(Optional.of(toiletOutboxEntity));

        subject.handler(event);

        verify(toiletOutboxSpringRepository).save(captor.capture());

        final var toiletOutboxEntityExpected = captor.getValue();
        Assertions
                .assertAll(
                        () -> Assertions.assertEquals(event.idempotentId(), toiletOutboxEntityExpected.getId()),
                        () -> Assertions.assertEquals(event.type(), toiletOutboxEntityExpected.getType()),
                        () -> Assertions.assertTrue(toiletOutboxEntityExpected.isPublished())
                );
    }

    @Test
    void shouldPublishCommandToOwnerWhenReceiveAToiletCreatedEvent() {
        final var toiletId = ToiletId.newId();
        final var customerId = CustomerId.newId();
        final var event = new ToiletCreatedEvent(toiletId, customerId);
        final var toiletOutboxEntity = new ToiletOutboxEntity(
                event.idempotentId(),
                event.type(),
                "{}",
                false,
                event.creationDate()
        );

        when(toiletOutboxSpringRepository.findById(event.idempotentId()))
                .thenReturn(Optional.of(toiletOutboxEntity));

        subject.handler(event);

        final var ownerExpected = new CreateOwnerCommand(customerId.value(), toiletId.value());
        verify(ownerCommandPublisher).publish(Mockito.eq(ownerExpected));
    }

    @Test
    void shouldNotPublishCommandToOwnerWhenReceiveAToiletCreatedEventButTheEventIsNotFound() {
        final var toiletId = ToiletId.newId();
        final var customerId = CustomerId.newId();

        final var event = new ToiletCreatedEvent(toiletId, customerId);

        when(toiletOutboxSpringRepository.findById(any()))
                .thenReturn(Optional.empty());

        subject.handler(event);

        verify(ownerCommandPublisher, never()).publish(Mockito.any());
    }
}