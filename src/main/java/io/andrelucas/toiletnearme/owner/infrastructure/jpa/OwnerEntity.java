package io.andrelucas.toiletnearme.owner.infrastructure.jpa;

import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.owner.business.Owner;
import io.andrelucas.toiletnearme.owner.business.OwnerId;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "owners")
public class OwnerEntity {

    public OwnerEntity() {
    }

    @Id
    private UUID id;
    private UUID customerId;
    private UUID toiletId;

    @Version
    private int version;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public OwnerEntity(final UUID id,
                       final UUID customerId,
                       final UUID toiletId,
                       final LocalDateTime updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.toiletId = toiletId;
        this.updatedAt = updatedAt;
    }

    public static OwnerEntity from(final Owner owner) {
        return new OwnerEntity(UUID.fromString(owner.ownerId().value()),
                UUID.fromString(owner.customerId().value()),
                UUID.fromString(owner.toiletId().value()),
                LocalDateTime.now());
    }

    public Owner toOwner() {
        return new Owner(OwnerId.with(id.toString()), CustomerId.with(customerId.toString()), ToiletId.with(toiletId.toString()));
    }
}
