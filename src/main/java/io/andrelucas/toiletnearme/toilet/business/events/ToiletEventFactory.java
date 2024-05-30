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
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> ToiletEvent createBy(final ToiletEvent event) {
        return switch (event){
            case ToiletCreatedEvent toiletCreated -> toiletCreated;

            default -> throw new IllegalArgumentException("Invalid event type: " + event);
        };
    }
}
