package io.andrelucas.toiletnearme.toilet.infrastructure.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.andrelucas.toiletnearme.toilet.business.events.*;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxEntity;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class ToiletOutboxRecurringJobTest {

    private ToiletOutboxRecurringJob toiletOutboxRecurringJob;
    private ToiletOutboxSpringRepository toiletOutboxSpringRepository;
    private ToiletEventPublisher toiletEventPublisher;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        toiletOutboxSpringRepository = mock(ToiletOutboxSpringRepository.class);
        toiletEventPublisher = mock(ToiletEventPublisher.class);
        toiletOutboxRecurringJob = new ToiletOutboxRecurringJob(toiletOutboxSpringRepository, toiletEventPublisher, new ToiletEventFactory(objectMapper));
    }

    @Test
    void shouldNotPublishEventsWhenReturnEmpty() {
        when(toiletOutboxSpringRepository.findAllByPublishedFalse()).thenReturn(Collections.emptyList());
        toiletOutboxRecurringJob.execute();

        verify(toiletEventPublisher, never()).publish(any());
    }

    @Test
    void shouldNotUpdateEventsWhenReturnEmpty() {
        when(toiletOutboxSpringRepository.findAllByPublishedFalse()).thenReturn(Collections.emptyList());
        toiletOutboxRecurringJob.execute();

        verify(toiletOutboxSpringRepository, never()).save(any());
    }

    @Test
    void shouldPublishAllEventsWhenContainsEventsMarkedAsNotPublishedYet() {
        final var captorEvent = ArgumentCaptor.forClass(ToiletEvent.class);
        final var outboxEntity = new ToiletOutboxEntity(
                UUID.fromString("7f3c8860-612c-4600-9915-0ed945c5ade7"),
                ToiletEventType.ToiletCreatedEvent,
                "{\"idempotentId\":\"7f3c8860-612c-4600-9915-0ed945c5ade7\",\"type\":\"ToiletCreatedEvent\",\"toiletId\":{\"value\":\"45670d5e-639b-414e-a81c-9b7d7e001ed7\"},\"customerId\":{\"value\":\"bf522dda-3001-4bcb-b48d-60d4ad61626a\"},\"creationDate\":\"2024-05-29T15:31:10.808684\"}",
                false,
                LocalDateTime.parse("2024-05-29T15:31:10.808684")
        );

        final var toiletOutboxEntities = List.of(
                outboxEntity
        );

        when(toiletOutboxSpringRepository.findAllByPublishedFalse()).thenReturn(toiletOutboxEntities);
        toiletOutboxRecurringJob.execute();

        verify(toiletEventPublisher, times(1)).publish(captorEvent.capture());
        final var event = captorEvent.getValue();
        Assertions.assertAll(
                () -> Assertions.assertEquals(outboxEntity.getId(), event.idempotentId()),
                () -> Assertions.assertEquals(outboxEntity.getType(), event.type()),
                () -> Assertions.assertEquals(outboxEntity.getType(), event.type()),
                () -> Assertions.assertInstanceOf(ToiletCreatedEvent.class, event),
                () -> Assertions.assertEquals(outboxEntity.getCreationDate(), event.creationDate())
        );
    }
}