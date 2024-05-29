package io.andrelucas.toiletnearme.toilet.business;

public class GeolocationInvalidException extends RuntimeException {
    public GeolocationInvalidException(String message) {
        super(message, null, false, false);
    }

    public GeolocationInvalidException(String message, Throwable cause) {
        super(message, cause, false, false);
    }


}
