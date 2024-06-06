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

    @Column(name = "version")
    @Version
    private Long version;

    @Column(name = "received")
    private boolean received;

    @Column(name = "created_at")
    private LocalDateTime creationDate;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ToiletOutboxEntity() {
    }

    public ToiletOutboxEntity(UUID id,
                              ToiletEventType type,
                              String content,
                              boolean published,
                              boolean received,
                              LocalDateTime creationDate,
                              LocalDateTime updatedAt) {
        this.id = id;
        this.type = type;
        this.content = content;
        this.published = published;
        this.received = received;
        this.creationDate = creationDate;
        this.updatedAt = updatedAt;
    }


    public static ToiletOutboxEntity of(final ToiletEvent toiletEvent,
                                        final String content) {
        return new ToiletOutboxEntity(
                toiletEvent.idempotentId(),
                toiletEvent.type(),
                content,
                false,
                false,
                toiletEvent.creationDate(),
                LocalDateTime.now()
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

    public ToiletEventType getType() {
        return type;
    }

    public boolean isPublished() {
        return published;
    }

    public ToiletOutboxEntity published() {
        this.published = true;
        this.received = false;
        this.updatedAt = LocalDateTime.now();

        return this;
    }

    public ToiletOutboxEntity received() {
        this.published = true;
        this.received = true;
        this.updatedAt = LocalDateTime.now();

        return this;
    }

    public boolean isReceived() {
        return received;
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
