package io.andrelucas.toiletnearme.customer.infrastructure.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CustomerSpringRepository extends CrudRepository<CustomerEntity, UUID> {
}
