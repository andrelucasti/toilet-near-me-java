package io.andrelucas.toiletnearme.owner.infrastructure.commands.listeners;

import io.andrelucas.toiletnearme.owner.business.commands.CreateOwnerCommand;
import io.andrelucas.toiletnearme.owner.business.usecases.CreateOwnerUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CreateOwnerCommandListener {
    private static final Logger logger = LoggerFactory.getLogger(CreateOwnerCommandListener.class);

    private final CreateOwnerUseCase createOwnerUseCase;

    public CreateOwnerCommandListener(CreateOwnerUseCase createOwnerUseCase) {
        this.createOwnerUseCase = createOwnerUseCase;
    }

    @EventListener
    public void handler(final CreateOwnerCommand command){
        logger.info("Received command: {}", command);

        final var input = new CreateOwnerUseCase.Input(command.customerId(), command.toiletId());
        createOwnerUseCase.execute(input);
    }
}
