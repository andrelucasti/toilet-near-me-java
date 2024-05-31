package io.andrelucas.toiletnearme.customer.business;

import io.andrelucas.toiletnearme.common.AggregateRoot;

public record Customer(CustomerId id,
                       String name,
                       String email) implements AggregateRoot {

    public static Customer create(String name, String email) {
        return new Customer(CustomerId.newId(), name, email);
    }
}
