package io.andrelucas.toiletnearme.toilet.infrastructure.events.internal.publishers;

import io.andrelucas.toiletnearme.toilet.business.events.ToiletEvent;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ToiletApplicationEventPublisher implements ToiletEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(ToiletApplicationEventPublisher.class);
    private final ApplicationEventPublisher applicationEventPublisher;

    public ToiletApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(final ToiletEvent event) {
        logger.info("Publishing event: {}", event);

        applicationEventPublisher.publishEvent(event);
    }
}
