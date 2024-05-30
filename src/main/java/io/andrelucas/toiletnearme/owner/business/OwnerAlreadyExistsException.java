package io.andrelucas.toiletnearme.owner.business;

public class OwnerAlreadyExistsException extends RuntimeException {
    public OwnerAlreadyExistsException(String message) {
        super(message, null, true, false);
    }

    public OwnerAlreadyExistsException(String message, Throwable cause) {
        super(message, cause, true, false);
    }
}
