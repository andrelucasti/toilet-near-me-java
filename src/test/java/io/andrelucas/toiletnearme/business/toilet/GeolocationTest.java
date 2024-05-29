package io.andrelucas.toiletnearme.business.toilet;

import io.andrelucas.toiletnearme.toilet.business.Geolocation;
import io.andrelucas.toiletnearme.toilet.business.GeolocationInvalidException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeolocationTest {

    @Test
    void shouldThrowExceptionWhenLatitudeIsInvalid() {
        assertThrows(GeolocationInvalidException.class, () -> new Geolocation(91.0, 0.0));
    }

    @Test
    void shouldThrowExceptionWhenLongitudeIsInvalid() {
        assertThrows(GeolocationInvalidException.class, () -> new Geolocation(0.0, 181.0));
    }

    @Test
    void shouldReturnGeolocationWhenLatitudeAndLongitudeIsValid() {
        final var geolocation = new Geolocation(12.6570, 99.023);

        assertAll(
                () -> assertEquals(12.6570, geolocation.latitude()),
                () -> assertEquals(99.023, geolocation.longitude())
        );
    }
}