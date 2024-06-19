package io.andrelucas.toiletnearme.toilet.business;

import io.andrelucas.toiletnearme.common.Tuple;
import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.toilet.business.events.ItemCreatedEvent;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletCreatedEvent;
import io.andrelucas.toiletnearme.common.AggregateRoot;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEvent;

import java.util.HashSet;
import java.util.Set;

public record Toilet(ToiletId id,
                     String name,
                     Geolocation geolocation,
                     Long price,
                     Set<Item> items,
                     Set<ToiletEvent> domainEvents) implements AggregateRoot {

    public Toilet(final ToiletId id,
                  final String name,
                  final Geolocation geolocation,
                  final long price,
                  final Set<Item> items) {

        this(id, name, geolocation, price, items, new HashSet<>());
    }

    public static Toilet newToilet(final String name,
                                   final double latitude,
                                   final double longitude,
                                   final long price,
                                   final CustomerId customerId) {

        final var toiletId = ToiletId.newId();
        final var geolocation = new Geolocation(latitude, longitude);
        final var toilet = new Toilet(toiletId, name, geolocation, price, new HashSet<>(), new HashSet<>());

        toilet.domainEvents().add(new ToiletCreatedEvent(toiletId, customerId));

        return toilet;
    }

    public Tuple<Toilet, Item> addItem(final String description) {
        final var newItem = Item.newItem(description);
        this.items.add(newItem);

        final var toilet = new Toilet(this.id, this.name, this.geolocation, this.price, this.items, this.domainEvents);
        toilet.domainEvents.add(new ItemCreatedEvent(newItem.id(), this.id));

        return new Tuple<>(toilet, newItem);
    }

    public ToiletType toiletType() {
        return this.price > 0 ? ToiletType.PAID : ToiletType.FREE;
    }
}
