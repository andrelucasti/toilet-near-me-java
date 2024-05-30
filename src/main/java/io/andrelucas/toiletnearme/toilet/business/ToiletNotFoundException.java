package io.andrelucas.toiletnearme.toilet.business;

public class ToiletNotFoundException extends RuntimeException {

    public ToiletNotFoundException(String message) {
        super(message, null, true, false);
    }

    public ToiletNotFoundException(String message, Throwable cause) {
        super(message, cause, true, false);
    }
}
