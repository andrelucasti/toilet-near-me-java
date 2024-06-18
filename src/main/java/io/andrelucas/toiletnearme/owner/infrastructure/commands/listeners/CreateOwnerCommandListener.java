package io.andrelucas.toiletnearme.owner.infrastructure.commands.listeners;

import io.andrelucas.toiletnearme.owner.business.commands.CreateOwnerCommand;
import io.andrelucas.toiletnearme.owner.business.usecases.CreateOwnerUseCase;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CreateOwnerCommandListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateOwnerCommandListener.class);

    private final CreateOwnerUseCase createOwnerUseCase;
    private final OpenTelemetry openTelemetry;

    public CreateOwnerCommandListener(CreateOwnerUseCase createOwnerUseCase, OpenTelemetry openTelemetry) {
        this.createOwnerUseCase = createOwnerUseCase;
        this.openTelemetry = openTelemetry;
    }

    @EventListener
    public void handler(final CreateOwnerCommand command){
        logger.info("Received command: {}", command);

        final var span = openTelemetry.getTracer("toilet-near-me-spring")
                .spanBuilder("CreateOwnerCommandListener")
                .setParent(Context.current())
                .setAttribute("command", command.toString())
                .startSpan();

        final var input = new CreateOwnerUseCase.Input(command.customerId(), command.toiletId());
        createOwnerUseCase.execute(input);

        span.end();
    }
}
