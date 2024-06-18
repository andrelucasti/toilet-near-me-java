package io.andrelucas.toiletnearme.owner.infrastructure.commands;

import io.andrelucas.toiletnearme.owner.business.commands.OwnerCommand;
import io.andrelucas.toiletnearme.owner.business.commands.OwnerCommandPublisher;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OwnerCommandApplicationPublisher implements OwnerCommandPublisher {
    private static final Logger logger = LoggerFactory.getLogger(OwnerCommandApplicationPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;
    private final OpenTelemetry openTelemetry;

    public OwnerCommandApplicationPublisher(final ApplicationEventPublisher applicationEventPublisher, OpenTelemetry openTelemetry) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.openTelemetry = openTelemetry;
    }

    @Override
    public void publish(final OwnerCommand ownerCommand) {
        logger.info("Publishing command: {}", ownerCommand);

        final var span = openTelemetry.getTracer("toilet-near-me-spring")
                .spanBuilder("OwnerCommandApplicationPublisher")
                .setParent(Context.current())
                .setAttribute("command", ownerCommand.toString())
                .startSpan();

        applicationEventPublisher.publishEvent(ownerCommand);

        span.end();
    }
}
