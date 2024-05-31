package io.andrelucas.toiletnearme.customer.business.usecases;

import io.andrelucas.toiletnearme.customer.business.Customer;
import io.andrelucas.toiletnearme.customer.business.CustomerRepository;

public class CreateCustomerUseCase {

    private final CustomerRepository customerRepository;

    public CreateCustomerUseCase(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Output execute(final Input input) {
        final var customer = Customer.create(input.name(), input.email());

        customerRepository.save(customer);

        return new Output(customer.id().value());
    }

    public record Input(String name, String email){}
    public record Output(String customerId){}
}