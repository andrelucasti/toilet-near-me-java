package io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa;

import io.andrelucas.toiletnearme.toilet.business.Item;
import io.andrelucas.toiletnearme.toilet.business.ItemId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "items")
public class ItemEntity {

    @Id
    private UUID id;

    private String description;
    @Version
    private int version;
    private LocalDateTime updatedAt;

    public ItemEntity() {
    }

    public ItemEntity(final UUID id,
                      final String description,
                      final LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.updatedAt = updatedAt;
    }

    public Item toItem() {
        return new Item(ItemId.with(id.toString()), description);
    }

    public static ItemEntity from(final Item item) {
        return new ItemEntity(UUID.fromString(item.id().value()), item.description(), LocalDateTime.now());
    }
}
