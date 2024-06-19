package io.andrelucas.toiletnearme.business.toilet.usecases;

import io.andrelucas.toiletnearme.AbstractIntegrationTest;
import io.andrelucas.toiletnearme.customer.business.Customer;
import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.toilet.business.ToiletType;
import io.andrelucas.toiletnearme.toilet.business.usecases.RegisterToiletUseCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class RegisterToiletUseCaseIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldRegisterAFreeToilet() {
        final var customerId = CustomerId.newId();
        final var customer = new Customer(customerId, "Customer", "bla@bla");
        customerRepository.save(customer);

        final var input = new RegisterToiletUseCase
                .Input("South Park", 1.0, 1.0, 0, customerId.value());

        final var subject = new RegisterToiletUseCase(toiletRepository, customerRepository);
        subject.execute(input);

        toiletRepository.findAll().stream()
                .findFirst()
                .ifPresentOrElse(toilet -> {
                    assertEquals("South Park", toilet.name());
                    assertEquals(1.0, toilet.geolocation().latitude());
                    assertEquals(1.0, toilet.geolocation().longitude());
                    assertEquals(0, toilet.price());
                    assertEquals(ToiletType.FREE, toilet.toiletType());
                }, () -> fail("Toilet not found"));
    }

    @Test
    void shouldRegisterAPaidToilet() {
        final var customerId = CustomerId.newId();
        final var customer = new Customer(customerId, "Customer", "bla@bla");
        customerRepository.save(customer);

        final var input = new RegisterToiletUseCase
                .Input("South Park", 1.0, 1.0, 100L, customerId.value());

        final var subject = new RegisterToiletUseCase(toiletRepository, customerRepository);
        subject.execute(input);

        toiletRepository.findAll().stream()
                .findFirst()
                .ifPresentOrElse(toilet -> {
                    assertEquals("South Park", toilet.name());
                    assertEquals(1.0, toilet.geolocation().latitude());
                    assertEquals(1.0, toilet.geolocation().longitude());
                    assertEquals(100L, toilet.price());
                    assertEquals(ToiletType.PAID, toilet.toiletType());
                }, () -> fail("Toilet not found"));
    }
}