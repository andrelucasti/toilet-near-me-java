package io.andrelucas.toiletnearme.common;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message, null, true, false);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause, true, false);
    }
}
