package io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa;

import io.andrelucas.toiletnearme.toilet.business.events.ToiletEvent;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEventType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "toilet_outbox")
public class ToiletOutboxEntity {
    @Id
    private UUID id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ToiletEventType type;

    @Column(name="content", columnDefinition = "JSON", length = 4_000)
    private String content;

    @Column(name = "published")
    private boolean published;

    @Column(name = "created_at")
    private LocalDateTime creationDate;

    public ToiletOutboxEntity() {
    }

    public ToiletOutboxEntity(UUID id,
                              ToiletEventType type,
                              String content,
                              boolean published,
                              LocalDateTime creationDate) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.published = published;
        this.creationDate = creationDate;
    }


    public static ToiletOutboxEntity of(final ToiletEvent toiletEvent,
                                        final String content) {
        return new ToiletOutboxEntity(
                toiletEvent.idempotentId(),
                toiletEvent.type(),
                content,
                false,
                toiletEvent.creationDate()
        );
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public UUID getId() {
        return id;
    }

    public boolean isPublished() {
        return published;
    }

    public ToiletEventType getType() {
        return type;
    }

    public ToiletOutboxEntity published() {
        this.published = true;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToiletOutboxEntity that = (ToiletOutboxEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
