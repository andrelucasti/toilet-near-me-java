package io.andrelucas.toiletnearme.toilet.business;

public record Geolocation(double latitude, double longitude) {
    public Geolocation {
        if (latitude < -90 || latitude > 90) {
            throw new GeolocationInvalidException("Latitude must be between -90 and 90");
        }

        if (longitude < -180 || longitude > 180) {
            throw new GeolocationInvalidException("Longitude must be between -180 and 180");
        }
    }
}
