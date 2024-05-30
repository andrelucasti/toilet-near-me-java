package io.andrelucas.toiletnearme.owner.infrastructure.commands;

import io.andrelucas.toiletnearme.owner.business.commands.OwnerCommand;
import io.andrelucas.toiletnearme.owner.business.commands.OwnerCommandPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OwnerCommandApplicationPublisher implements OwnerCommandPublisher {
    private static final Logger logger = LoggerFactory.getLogger(OwnerCommandApplicationPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public OwnerCommandApplicationPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(final OwnerCommand ownerCommand) {
        logger.info("Publishing command: {}", ownerCommand);
        applicationEventPublisher.publishEvent(ownerCommand);
    }
}
