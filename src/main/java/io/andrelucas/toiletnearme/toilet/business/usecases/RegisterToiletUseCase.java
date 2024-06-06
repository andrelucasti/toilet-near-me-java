package io.andrelucas.toiletnearme.toilet.business.usecases;

import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.customer.business.CustomerNotFound;
import io.andrelucas.toiletnearme.customer.business.CustomerRepository;
import io.andrelucas.toiletnearme.toilet.business.Toilet;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;

public class RegisterToiletUseCase {

    private final ToiletRepository toiletRepository;
    private final CustomerRepository customerRepository;

    public RegisterToiletUseCase(final ToiletRepository toiletRepository,
                                 final CustomerRepository customerRepository) {

        this.toiletRepository = toiletRepository;
        this.customerRepository = customerRepository;
    }

    //Maybe we can use EITHER data structure
    public Output execute(final Input input){
        final var customerId = CustomerId.with(input.customerId);

        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFound("Customer does not exist"));

        final var newToilet = Toilet.newToilet(input.name(), input.latitude(), input.longitude(), customerId);
        toiletRepository.save(newToilet);

        return new Output(newToilet.id().value());
    }

    public record Input(String name, double latitude, double longitude, String customerId) {}
    public record Output(String toiletId) {}
}
