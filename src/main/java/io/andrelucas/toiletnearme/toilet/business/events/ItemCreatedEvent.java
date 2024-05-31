package io.andrelucas.toiletnearme.toilet.business.events;

import io.andrelucas.toiletnearme.toilet.business.ItemId;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record ItemCreatedEvent(UUID idempotentId,
                               ToiletEventType type,
                               ItemId itemId,
                               ToiletId toiletId,
                               LocalDateTime creationDate) implements ToiletEvent {

    public ItemCreatedEvent(final ItemId itemId,
                            final ToiletId toiletId){

        this(UUID.randomUUID(), ToiletEventType.ItemCreatedEvent, itemId, toiletId, LocalDateTime.now());
    }

    @Override
    public Map<String, String> attributes() {
        return Map.of("itemId", itemId().value(),
                      "toiletId", toiletId().value());
    }
}
