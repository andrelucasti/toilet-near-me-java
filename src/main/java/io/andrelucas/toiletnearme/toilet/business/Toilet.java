package io.andrelucas.toiletnearme.toilet.business;

import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletCreatedEvent;
import io.andrelucas.toiletnearme.common.AggregateRoot;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEvent;

import java.util.HashSet;
import java.util.Set;

public record Toilet(ToiletId id,
                     String name,
                     Geolocation geolocation,
                     Set<ToiletEvent> domainEvents) implements AggregateRoot {

    public Toilet(final ToiletId id,
                  final String name,
                  final Geolocation geolocation) {

        this(id, name, geolocation, new HashSet<>());
    }


    public static Toilet newToilet(final String name,
                                   final double latitude,
                                   final double longitude,
                                   final CustomerId customerId) {

        final var geolocation = new Geolocation(latitude, longitude);
        final var toiletId = ToiletId.newId();

        final var toilet = new Toilet(toiletId, name, geolocation);

        toilet.domainEvents().add(new ToiletCreatedEvent(toiletId, customerId));

        return toilet;
    }
}
