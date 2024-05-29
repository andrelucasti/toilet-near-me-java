package io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa;

import io.andrelucas.toiletnearme.toilet.business.Geolocation;
import io.andrelucas.toiletnearme.toilet.business.Toilet;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Version
    private int version;

    private LocalDateTime updatedAt;

    public ToiletEntity() {
    }

    public ToiletEntity(final UUID id,
                        final String description,
                        final Double latitude,
                        final Double longitude,
                        final LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public static ToiletEntity from(final Toilet toilet) {
        return new ToiletEntity(UUID.fromString(toilet.id().value()),
                toilet.name(),
                toilet.geolocation().latitude(),
                toilet.geolocation().longitude(),
                LocalDateTime.now());
    }

    public Toilet toToilet() {
        final var geolocation = new Geolocation(latitude, longitude);
        return new Toilet(ToiletId.with(id.toString()), description, geolocation);
    }
}
