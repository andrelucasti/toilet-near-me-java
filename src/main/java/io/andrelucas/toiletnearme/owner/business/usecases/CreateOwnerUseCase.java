package io.andrelucas.toiletnearme.owner.business.usecases;

import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.customer.business.CustomerNotFound;
import io.andrelucas.toiletnearme.customer.business.CustomerRepository;
import io.andrelucas.toiletnearme.owner.business.Owner;
import io.andrelucas.toiletnearme.owner.business.OwnerAlreadyExistsException;
import io.andrelucas.toiletnearme.owner.business.OwnerRepository;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import io.andrelucas.toiletnearme.toilet.business.ToiletNotFoundException;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;

public class CreateOwnerUseCase {
    private final CustomerRepository customerRepository;
    private final ToiletRepository toiletRepository;
    private final OwnerRepository ownerRepository;

    public CreateOwnerUseCase(final CustomerRepository customerRepository,
                              final ToiletRepository toiletRepository,
                              final OwnerRepository ownerRepository) {

        this.customerRepository = customerRepository;
        this.toiletRepository = toiletRepository;
        this.ownerRepository = ownerRepository;
    }

    public void execute(final Input input){
        customerRepository.findById(CustomerId.with(input.customerId()))
                .orElseThrow(() -> new CustomerNotFound("Customer does not exist"));

        toiletRepository.findById(ToiletId.with(input.toiletId()))
                .orElseThrow(() -> new ToiletNotFoundException("Toilet does not exist"));

        ownerRepository.findByToiletId(ToiletId.with(input.toiletId()))
                .ifPresent(owner -> {
                    throw new OwnerAlreadyExistsException("Owner already exists");
                });

        final var owner = Owner.create(CustomerId.with(input.customerId()), ToiletId.with(input.toiletId()));
        ownerRepository.save(owner);
    }

    public record Input(String customerId, String toiletId){}
}
