package io.andrelucas.toiletnearme.business.toilet;

import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.toilet.business.GeolocationInvalidException;
import io.andrelucas.toiletnearme.toilet.business.Item;
import io.andrelucas.toiletnearme.toilet.business.Toilet;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletCreatedEvent;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEvent;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ToiletTest {

    @Test
    void shouldReturnANewToilet() {
        final var customerId = CustomerId.newId();
        final var toilet = Toilet.newToilet("Toilet", 0.0, 0.0, customerId);

        assertNotNull(toilet.id());
        assertEquals("Toilet", toilet.name());
        assertEquals(0.0, toilet.geolocation().latitude());
        assertEquals(0.0, toilet.geolocation().longitude());
    }

    @Test
    void shouldThrowExceptionWhenLatitudeIsInvalid() {
        Assertions.assertThrows(GeolocationInvalidException.class, () -> Toilet.newToilet("Toilet", 91.0, 0.0,  CustomerId.newId()));
    }

    @Test
    void shouldThrowExceptionWhenLongitudeIsInvalid() {
        Assertions.assertThrows(GeolocationInvalidException.class, () -> Toilet.newToilet("Toilet", 0.0, 181.0, CustomerId.newId()));
    }

    @Test
    void shouldSaveTheDomainEventToiletCreatedWhenAToiletIsCreated() {
        final var customerId = CustomerId.newId();
        final var toilet = Toilet.newToilet("Toilet", 33.065, 89.0, customerId);


        Assertions.assertAll(
            () -> assertNotNull(toilet.id()),
            () -> assertEquals("Toilet", toilet.name()),
            () -> assertEquals(33.065, toilet.geolocation().latitude()),
            () -> assertEquals(89.0, toilet.geolocation().longitude())
        );

        assertEquals(1, toilet.domainEvents().size());

        toilet.domainEvents().stream()
            .findFirst()
            .ifPresentOrElse(domainEvent -> {
                if (domainEvent instanceof ToiletCreatedEvent event) {
                    assertEquals(toilet.id(), event.toiletId());
                    assertEquals(customerId, event.customerId());
                    assertEquals(ToiletEventType.ToiletCreatedEvent, event.type());
                } else {
                    Assertions.fail("Domain event not found");
                }

                }, () -> Assertions.fail("Domain event not found"));
    }

    @Test
    void shouldReturnANewToiletAndAToiletItemWhenAItemIsCreated() {
        final var customerId = CustomerId.newId();
        final var toilet = Toilet.newToilet("Toilet", 0.0, 0.0, customerId);
        final var toiletWithNewItem = toilet.addItem("Soap")._1();

        assertNotNull(toilet.id());
        assertEquals("Toilet", toilet.name());
        assertEquals(0.0, toilet.geolocation().latitude());
        assertEquals(0.0, toilet.geolocation().longitude());

        assertEquals(toilet.id(), toiletWithNewItem.id());
        assertEquals("Toilet", toiletWithNewItem.name());
        assertEquals(0.0, toiletWithNewItem.geolocation().latitude());
        assertEquals(0.0, toiletWithNewItem.geolocation().longitude());
        assertEquals(1, toiletWithNewItem.items().size());
        assertEquals("Soap", toiletWithNewItem.items().stream().findFirst().get().description());
    }

    @Test
    void shouldSaveTheDomainEventWhenAnItemIsAdded() {
        final var customerId = CustomerId.newId();
        final var toilet = Toilet.newToilet("Toilet", 0.0, 0.0, customerId);
        final var toiletWithNewItem = toilet.addItem("Soap")._1();

        final var itemId = toiletWithNewItem.items().stream()
                .findFirst()
                .map(Item::id)
                .orElseThrow();

        final var eventTypes = toiletWithNewItem.domainEvents().stream()
                .map(ToiletEvent::type).toList();
        final var attributes = toiletWithNewItem.domainEvents().stream()
                .flatMap(it-> it.attributes().values().stream())
                .collect(Collectors.toSet());

        assertEquals(2, toiletWithNewItem.domainEvents().size());
        assertThat(eventTypes)
                .containsOnly(
                        ToiletEventType.ToiletCreatedEvent,
                        ToiletEventType.ItemCreatedEvent
                );

        assertThat(attributes)
                .containsOnly(toilet.id().value(), customerId.value(), itemId.value());
    }
}