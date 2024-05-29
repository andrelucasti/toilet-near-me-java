package io.andrelucas.toiletnearme.toilet.business;

import io.andrelucas.toiletnearme.common.UUIDGenerator;

public record ToiletId(String value) {

    public static ToiletId with(String value) {
        return new ToiletId(value);
    }

    public static ToiletId newId() {
        return new ToiletId(UUIDGenerator.generateUUID());
    }
}
