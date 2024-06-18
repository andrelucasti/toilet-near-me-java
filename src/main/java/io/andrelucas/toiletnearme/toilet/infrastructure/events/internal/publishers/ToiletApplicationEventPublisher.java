package io.andrelucas.toiletnearme.toilet.infrastructure.events.internal.publishers;

import io.andrelucas.toiletnearme.toilet.business.events.ToiletEvent;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEventPublisher;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ToiletApplicationEventPublisher implements ToiletEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(ToiletApplicationEventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ToiletOutboxSpringRepository toiletOutboxSpringRepository;
    private final OpenTelemetry openTelemetry;

    public ToiletApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher,
                                           final ToiletOutboxSpringRepository toiletOutboxSpringRepository, OpenTelemetry openTelemetry) {

        this.applicationEventPublisher = applicationEventPublisher;
        this.toiletOutboxSpringRepository = toiletOutboxSpringRepository;
        this.openTelemetry = openTelemetry;
    }

    @Override
    @Transactional
    public void publish(final ToiletEvent event) {
        logger.info("Publishing event: {}", event);
        final var span = openTelemetry.getTracer("toilet-near-me-spring")
                .spanBuilder(event.type().name())
                .setNoParent()
                .setAttribute("domain.event", event.type().name())
                .setAttribute("publisher", "publisher")
                .setAttribute("idempotentId", event.idempotentId().toString())
                .startSpan();

        try(final var scope = span.makeCurrent()) {
            toiletOutboxSpringRepository.findById(event.idempotentId())
                    .ifPresent(toiletOutboxEntity -> toiletOutboxSpringRepository.save(toiletOutboxEntity.published()));

            applicationEventPublisher.publishEvent(event);

        } finally {
            span.end();
        }
    }
}
