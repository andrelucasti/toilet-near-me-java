package io.andrelucas.toiletnearme.toilet.infrastructure.events.internal.listeners;

import io.andrelucas.toiletnearme.owner.business.commands.CreateOwnerCommand;
import io.andrelucas.toiletnearme.owner.business.commands.OwnerCommandPublisher;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletCreatedEvent;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ToiletCreatedEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ToiletCreatedEventListener.class);
    private final ToiletOutboxSpringRepository toiletOutboxSpringRepository;
    private final OwnerCommandPublisher ownerCommandPublisher;

    public ToiletCreatedEventListener(final ToiletOutboxSpringRepository toiletOutboxSpringRepository,
                                      final OwnerCommandPublisher ownerCommandPublisher,
                                      final OpenTelemetry openTelemetry) {
        this.ownerCommandPublisher = ownerCommandPublisher;
        this.toiletOutboxSpringRepository = toiletOutboxSpringRepository;
    }

    @EventListener
    public void handler(final ToiletCreatedEvent event) {
        logger.info("Received event: {}", event);
        toiletOutboxSpringRepository.findById(event.idempotentId())
                .ifPresent(toiletOutboxEntity -> {
                    toiletOutboxSpringRepository.save(toiletOutboxEntity.received());

                    final var  createOwnerCommand = new CreateOwnerCommand(event.customerId().value(), event.toiletId().value());

                    logger.info("Sending command: {}", createOwnerCommand);
                    ownerCommandPublisher.publish(createOwnerCommand);
                });

    }
}
