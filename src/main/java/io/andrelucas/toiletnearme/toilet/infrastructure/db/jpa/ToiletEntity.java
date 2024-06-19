package io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa;

import io.andrelucas.toiletnearme.toilet.business.Geolocation;
import io.andrelucas.toiletnearme.toilet.business.Toilet;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import io.andrelucas.toiletnearme.toilet.business.ToiletType;
import jakarta.persistence.*;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
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

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ToiletType type;

    @Column(name = "price")
    private Long price;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "toilet_id")
    private Set<ItemEntity> items = new HashSet<>();

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
                        final ToiletType type,
                        final Long price,
                        final LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.price = price;
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

    public void addItem(final ItemEntity itemEntity) {
        items.add(itemEntity);
    }
    public static ToiletEntity from(final Toilet toilet) {
        final var itemsEntities = toilet.items().stream()
                .map(ItemEntity::from)
                .collect(Collectors.toSet());

        final var toiletEntity = new ToiletEntity(UUID.fromString(toilet.id().value()),
                toilet.name(),
                toilet.geolocation().latitude(),
                toilet.geolocation().longitude(),
                toilet.toiletType(),
                toilet.price(),
                LocalDateTime.now());

        itemsEntities.forEach(toiletEntity::addItem);

        return toiletEntity;
    }

    public Toilet toToilet() {
        final var geolocation = new Geolocation(latitude, longitude);
        final var items = this.items.stream()
                .map(ItemEntity::toItem)
                .collect(Collectors.toSet());

        return new Toilet(ToiletId.with(id.toString()), description, geolocation, price, items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ToiletEntity that = (ToiletEntity) o;
        return version == that.version && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + version;
        return result;
    }
}
