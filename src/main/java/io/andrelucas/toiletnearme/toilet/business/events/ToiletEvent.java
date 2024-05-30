package io.andrelucas.toiletnearme.toilet.business.events;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ToiletEvent {
    UUID idempotentId();
    ToiletEventType type();
    LocalDateTime creationDate();
}
