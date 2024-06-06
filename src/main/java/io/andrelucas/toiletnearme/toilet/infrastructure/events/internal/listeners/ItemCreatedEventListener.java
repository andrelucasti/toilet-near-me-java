package io.andrelucas.toiletnearme.toilet.infrastructure.events.internal.listeners;

import io.andrelucas.toiletnearme.toilet.business.events.ItemCreatedEvent;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ItemCreatedEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ItemCreatedEventListener.class);

    private final ToiletOutboxSpringRepository toiletOutboxSpringRepository;

    public ItemCreatedEventListener(ToiletOutboxSpringRepository toiletOutboxSpringRepository) {
        this.toiletOutboxSpringRepository = toiletOutboxSpringRepository;
    }

    @EventListener
    public void handler(final ItemCreatedEvent event) {
        logger.info("Received event: {}", event);

        toiletOutboxSpringRepository.findById(event.idempotentId())
                .ifPresent(toiletOutboxEntity ->
                        toiletOutboxSpringRepository.save(toiletOutboxEntity.received()));
    }

}
