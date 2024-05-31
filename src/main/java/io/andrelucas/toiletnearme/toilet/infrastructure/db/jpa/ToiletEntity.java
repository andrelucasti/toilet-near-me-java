package io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa;

import io.andrelucas.toiletnearme.toilet.business.Geolocation;
import io.andrelucas.toiletnearme.toilet.business.Toilet;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import jakarta.persistence.*;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "toilets")
public class ToiletEntity {

    @Id
    private UUID id;

    @Column(name = "description")
    private String description;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @OneToMany
    private Set<ItemEntity> items;

    @Version
    private int version;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public ToiletEntity() {
    }

    public ToiletEntity(final UUID id,
                        final String description,
                        final Double latitude,
                        final Double longitude,
                        final Set<ItemEntity> items,
                        final LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.items = items;
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public UUID getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Set<ItemEntity> getItems() {
        return items;
    }

    public static ToiletEntity from(final Toilet toilet) {
        final var itemsEntities = toilet.items().stream()
                .map(ItemEntity::from)
                .collect(Collectors.toSet());

        return new ToiletEntity(UUID.fromString(toilet.id().value()),
                toilet.name(),
                toilet.geolocation().latitude(),
                toilet.geolocation().longitude(),
                itemsEntities,
                LocalDateTime.now());
    }

    public Toilet toToilet() {
        final var geolocation = new Geolocation(latitude, longitude);
        final var items = this.items.stream()
                .map(ItemEntity::toItem)
                .collect(Collectors.toSet());

        return new Toilet(ToiletId.with(id.toString()), description, geolocation, items);
    }
}
