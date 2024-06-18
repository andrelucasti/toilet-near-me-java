package io.andrelucas.toiletnearme.toilet.infrastructure.events.internal.listeners;

import io.andrelucas.toiletnearme.toilet.business.events.ItemCreatedEvent;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ItemCreatedEventListener {
    private static final Logger logger = LoggerFactory.getLogger(ItemCreatedEventListener.class);

    private final ToiletOutboxSpringRepository toiletOutboxSpringRepository;
    private final OpenTelemetry openTelemetry;

    public ItemCreatedEventListener(ToiletOutboxSpringRepository toiletOutboxSpringRepository, OpenTelemetry openTelemetry) {
        this.toiletOutboxSpringRepository = toiletOutboxSpringRepository;
        this.openTelemetry = openTelemetry;
    }

    @EventListener
    public void handler(final ItemCreatedEvent event) {
        logger.info("Received event: {}", event);
        final var span = openTelemetry.getTracer("toilet-near-me-spring")
                .spanBuilder("ItemCreatedEventListener")
                .setParent(Context.current())
                .setAttribute("event", event.type().name())
                .setAttribute("listener", "listener")
                .setAttribute("idempotentId", event.idempotentId().toString())
                .startSpan();

        toiletOutboxSpringRepository.findById(event.idempotentId())
                .ifPresent(toiletOutboxEntity ->
                        toiletOutboxSpringRepository.save(toiletOutboxEntity.received()));

        span.end();
    }

}
