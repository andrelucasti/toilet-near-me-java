package io.andrelucas.toiletnearme.toilet.infrastructure.events.internal.publishers;

import io.andrelucas.toiletnearme.toilet.business.events.ToiletEvent;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEventPublisher;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ToiletApplicationEventPublisher implements ToiletEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(ToiletApplicationEventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ToiletOutboxSpringRepository toiletOutboxSpringRepository;

    public ToiletApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher,
                                           final ToiletOutboxSpringRepository toiletOutboxSpringRepository) {

        this.applicationEventPublisher = applicationEventPublisher;
        this.toiletOutboxSpringRepository = toiletOutboxSpringRepository;
    }

    @Override
    public void publish(final ToiletEvent event) {
        logger.info("Publishing event: {}", event);

        toiletOutboxSpringRepository.findById(event.idempotentId())
                .ifPresent(toiletOutboxEntity -> toiletOutboxSpringRepository.save(toiletOutboxEntity.published()));

        applicationEventPublisher.publishEvent(event);
    }
}
