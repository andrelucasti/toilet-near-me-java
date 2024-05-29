package io.andrelucas.toiletnearme.customer;

import io.andrelucas.toiletnearme.common.AggregateRoot;

public record Customer(CustomerId id,
                       String name,
                       String email) implements AggregateRoot {
}
