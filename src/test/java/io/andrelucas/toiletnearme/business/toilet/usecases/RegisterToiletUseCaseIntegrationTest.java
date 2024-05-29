package io.andrelucas.toiletnearme.business.toilet.usecases;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.andrelucas.toiletnearme.customer.Customer;
import io.andrelucas.toiletnearme.customer.CustomerId;
import io.andrelucas.toiletnearme.customer.CustomerRepository;
import io.andrelucas.toiletnearme.infrastructure.inmemory.InMemoryCustomerRepository;
import io.andrelucas.toiletnearme.toilet.business.ToiletRepository;
import io.andrelucas.toiletnearme.toilet.business.usecases.RegisterToiletUseCase;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletRepositoryJPAImpl;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletSpringRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class RegisterToiletUseCaseIntegrationTest {

    private ToiletRepository toiletRepository;
    private CustomerRepository customerRepository;

    @Autowired
    private ToiletSpringRepository toiletSpringRepository;

    @Autowired
    private ToiletOutboxSpringRepository toiletOutboxSpringRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        toiletSpringRepository.deleteAll();
        toiletRepository = new ToiletRepositoryJPAImpl(toiletSpringRepository, toiletOutboxSpringRepository, objectMapper);

        customerRepository = new InMemoryCustomerRepository();
    }

    @Test
    void shouldRegisterToilet() {
        final var customerId = CustomerId.newId();
        final var customer = new Customer(customerId, "Customer", "bla@bla");
        customerRepository.save(customer);

        final var input = new RegisterToiletUseCase
                .Input("South Park", 1.0, 1.0, customerId.value());

        final var subject = new RegisterToiletUseCase(toiletRepository, customerRepository);
        subject.execute(input);

        toiletRepository.findAll().stream()
                .findFirst()
                .ifPresentOrElse(toilet -> {
                    assertEquals("South Park", toilet.name());
                    assertEquals(1.0, toilet.geolocation().latitude());
                    assertEquals(1.0, toilet.geolocation().longitude());
                }, () -> fail("Toilet not found"));
    }
}