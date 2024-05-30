package io.andrelucas.toiletnearme.owner.business;

import io.andrelucas.toiletnearme.common.UUIDGenerator;

public record OwnerId(String value) {

    public static OwnerId with(String value) {
        return new OwnerId(value);
    }

    public static OwnerId newId() {
        return new OwnerId(UUIDGenerator.generateUUID());
    }
}
