package io.andrelucas.toiletnearme.toilet.business;

import io.andrelucas.toiletnearme.common.UUIDGenerator;

public record ItemId(String value) {

    public static ItemId with(String value) {
        return new ItemId(value);
    }

    public static ItemId newId() {
        return new ItemId(UUIDGenerator.generateUUID());
    }
}
