package io.andrelucas.toiletnearme.toilet.business.events;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public interface ToiletEvent {
    UUID idempotentId();
    String type();
    LocalDateTime creationDate();
}
