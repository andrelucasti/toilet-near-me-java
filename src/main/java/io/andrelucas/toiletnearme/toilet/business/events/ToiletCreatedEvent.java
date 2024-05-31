package io.andrelucas.toiletnearme.toilet.business.events;

import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;

import java.time.LocalDateTime;
import java.util.UUID;

public record ToiletCreatedEvent(UUID idempotentId,
                                 ToiletEventType type,
                                 ToiletId toiletId,
                                 CustomerId customerId,
                                 LocalDateTime creationDate) implements ToiletEvent {

    public ToiletCreatedEvent(final ToiletId toiletId,
                              final CustomerId customerId) {

        this(UUID.randomUUID(), ToiletEventType.ToiletCreatedEvent, toiletId, customerId, LocalDateTime.now());
    }
}
