package io.andrelucas.toiletnearme.customer.infrastructure.jpa;

import io.andrelucas.toiletnearme.customer.Customer;
import io.andrelucas.toiletnearme.customer.CustomerId;
import io.andrelucas.toiletnearme.customer.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Repository
public class CustomerRepositoryJPAImpl implements CustomerRepository {

    private final CustomerSpringRepository customerSpringRepository;

    public CustomerRepositoryJPAImpl(final CustomerSpringRepository customerSpringRepository) {
        this.customerSpringRepository = customerSpringRepository;
    }

    @Override
    public void save(final Customer entity) {
        customerSpringRepository.save(CustomerEntity.from(entity));
    }

    @Override
    public Optional<Customer> findById(final CustomerId customerId) {
        return customerSpringRepository
                .findById(UUID.fromString(customerId.value()))
                .map(CustomerEntity::toCustomer);

    }

    @Override
    public List<Customer> findAll() {
        return StreamSupport.stream(customerSpringRepository.findAll().spliterator(), false)
                .map(CustomerEntity::toCustomer)
                .toList();
    }

    @Override
    public void delete(final Customer entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void update(final Customer entity) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
