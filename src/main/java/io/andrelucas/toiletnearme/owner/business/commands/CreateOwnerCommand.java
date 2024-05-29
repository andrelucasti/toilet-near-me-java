package io.andrelucas.toiletnearme.owner.business.commands;

public record CreateOwnerCommand(String customerId, String toiletId) implements OwnerCommand{
}
