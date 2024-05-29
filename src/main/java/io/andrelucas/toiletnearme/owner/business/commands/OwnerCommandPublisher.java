package io.andrelucas.toiletnearme.owner.business.commands;

public interface OwnerCommandPublisher {

    void publish(OwnerCommand ownerCommand);
}
