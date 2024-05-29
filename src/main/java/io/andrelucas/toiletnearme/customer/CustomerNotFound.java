package io.andrelucas.toiletnearme.customer;

public class CustomerNotFound extends RuntimeException {

        public CustomerNotFound(String message) {
            super(message, null, true, false);
        }
}
