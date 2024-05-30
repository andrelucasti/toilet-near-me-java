package io.andrelucas.toiletnearme.customer.infrastructure.jpa;

import io.andrelucas.toiletnearme.customer.Customer;
import io.andrelucas.toiletnearme.customer.CustomerId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "customers")
public class CustomerEntity {
    @Id
    private UUID id;
    private String name;
    private String email;

    @Version
    private int version;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public CustomerEntity() {
    }

    public CustomerEntity(final UUID id,
                          final String name,
                          final String email,
                          final LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.updatedAt = updatedAt;
    }

    public Customer toCustomer() {
        return new Customer(CustomerId.with(id.toString()), name, email);
    }

    public static CustomerEntity from(final Customer customer) {
        return new CustomerEntity(UUID.fromString(customer.id().value()), customer.name(), customer.email(), LocalDateTime.now());
    }


}
