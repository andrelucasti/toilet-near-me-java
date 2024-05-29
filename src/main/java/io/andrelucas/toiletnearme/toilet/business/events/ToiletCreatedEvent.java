package io.andrelucas.toiletnearme.toilet.business.events;

import io.andrelucas.toiletnearme.customer.CustomerId;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;

import java.time.LocalDateTime;
import java.util.UUID;

public record ToiletCreatedEvent(UUID idempotentId,
                                 String type,
                                 ToiletId toiletId,
                                 CustomerId customerId,
                                 LocalDateTime creationDate) implements ToiletEvent {

    public ToiletCreatedEvent(ToiletId toiletId, CustomerId customerId) {
        this(UUID.randomUUID(), "ToiletCreatedEvent", toiletId, customerId, LocalDateTime.now());
    }
}
