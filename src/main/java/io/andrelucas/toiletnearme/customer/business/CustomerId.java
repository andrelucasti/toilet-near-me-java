package io.andrelucas.toiletnearme.customer.business;

import io.andrelucas.toiletnearme.common.UUIDGenerator;
import io.andrelucas.toiletnearme.common.ValidationException;

import java.util.UUID;

public record CustomerId(String value) {

    public static CustomerId with(String value) {
        try {
            return new CustomerId(UUID.fromString(value).toString());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid UUID value: " + value);
        }
    }

    public static CustomerId newId() {
        return new CustomerId(UUIDGenerator.generateUUID());
    }
}
