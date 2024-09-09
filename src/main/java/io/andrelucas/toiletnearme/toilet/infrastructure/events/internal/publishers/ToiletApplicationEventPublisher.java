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

    public ToiletApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher,
                                           final ToiletOutboxSpringRepository toiletOutboxSpringRepository, OpenTelemetry openTelemetry) {

        this.applicationEventPublisher = applicationEventPublisher;
        this.toiletOutboxSpringRepository = toiletOutboxSpringRepository;
    }

    @Override
    @Transactional
    public void publish(final ToiletEvent event) {
        logger.info("Publishing event: {}", event);

        toiletOutboxSpringRepository.findById(event.idempotentId())
                .ifPresent(toiletOutboxEntity -> toiletOutboxSpringRepository.save(toiletOutboxEntity.published()));

        applicationEventPublisher.publishEvent(event);


    }
}
