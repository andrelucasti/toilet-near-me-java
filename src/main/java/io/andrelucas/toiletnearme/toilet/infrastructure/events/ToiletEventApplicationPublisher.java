package io.andrelucas.toiletnearme.toilet.infrastructure.events;

import io.andrelucas.toiletnearme.toilet.business.events.ToiletCreatedEvent;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEvent;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ToiletEventApplicationPublisher implements ToiletEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(ToiletEventApplicationPublisher.class);
    private final ApplicationEventPublisher applicationEventPublisher;

    public ToiletEventApplicationPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(final ToiletEvent event) {
        logger.info("Publishing event: {}", event);

        switch (event){
            case ToiletCreatedEvent toiletCreated -> applicationEventPublisher.publishEvent(toiletCreated);
            default -> throw new IllegalArgumentException("Invalid event type: " + event);
        }

        //applicationEventPublisher.publishEvent(event);
    }
}
