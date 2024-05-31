package io.andrelucas.toiletnearme.owner.business;

import io.andrelucas.toiletnearme.common.AggregateRoot;
import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;

public record Owner(OwnerId ownerId,
                    CustomerId customerId,
                    ToiletId toiletId) implements AggregateRoot {

    public static Owner create(final CustomerId customerId,
                               final ToiletId toiletId) {

        return new Owner(OwnerId.newId(), customerId, toiletId);
    }
}
