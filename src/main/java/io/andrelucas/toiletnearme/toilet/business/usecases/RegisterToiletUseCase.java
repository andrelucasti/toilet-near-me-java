package io.andrelucas.toiletnearme.toilet.business.usecases;

import io.andrelucas.toiletnearme.customer.CustomerId;
import io.andrelucas.toiletnearme.customer.CustomerNotFound;
import io.andrelucas.toiletnearme.customer.CustomerRepository;
import io.andrelucas.toiletnearme.toilet.business.Toilet;
import io.andrelucas.toiletnearme.toilet.business.ToiletRepository;

public class RegisterToiletUseCase {

    private final ToiletRepository toiletRepository;
    private final CustomerRepository customerRepository;

    public RegisterToiletUseCase(final ToiletRepository toiletRepository,
                                 final CustomerRepository customerRepository) {

        this.toiletRepository = toiletRepository;
        this.customerRepository = customerRepository;
    }

    //Maybe we can use EITHER data structure
    public void execute(final Input input){
        final var customerId = CustomerId.with(input.customerId);

        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFound("Customer does not exist"));

        toiletRepository.save(
                Toilet.newToilet(input.name(), input.latitude(), input.longitude(), customerId)
        );
    }

    public record Input(String name, double latitude, double longitude, String customerId) {}
}
