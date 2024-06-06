package io.andrelucas.toiletnearme.toilet.infrastructure.controllers.requests;

public record CreateToiletRequest(String description,
                                  double latitude,
                                  double longitude,
                                  String customerId){}
