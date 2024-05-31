package io.andrelucas.toiletnearme.toilet.business.events;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ToiletEventFactory {
    private final ObjectMapper objectMapper;

    public ToiletEventFactory(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ToiletEvent createBy(final ToiletEventType eventType,
                                final String content) {
        try {
            return switch (eventType) {
                case ToiletCreatedEvent -> objectMapper.readValue(content, ToiletCreatedEvent.class);
                case ItemCreatedEvent -> objectMapper.readValue(content, ItemCreatedEvent.class);
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
