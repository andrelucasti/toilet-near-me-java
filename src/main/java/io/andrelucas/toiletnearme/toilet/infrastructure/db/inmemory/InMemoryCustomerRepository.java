package io.andrelucas.toiletnearme.toilet.infrastructure.db.inmemory;

import io.andrelucas.toiletnearme.customer.Customer;
import io.andrelucas.toiletnearme.customer.CustomerId;
import io.andrelucas.toiletnearme.customer.CustomerRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryCustomerRepository implements CustomerRepository {
    private final Map<CustomerId, Customer> toilets = new HashMap<>();

    @Override
    public void delete(Customer entity) {
        toilets.remove(entity.id());
    }

    @Override
    public List<Customer> findAll() {
        return List.copyOf(toilets.values());
    }

    @Override
    public Optional<Customer> findById(CustomerId toiletId) {
        return Optional.ofNullable(toilets.get(toiletId));
    }

    @Override
    public void save(Customer entity) {
        toilets.put(entity.id(), entity);
    }

    @Override
    public void update(Customer entity) {
        toilets.replace(entity.id(), entity);
    }
}
