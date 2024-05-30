package io.andrelucas.toiletnearme.business.toilet.usecases;

import io.andrelucas.toiletnearme.customer.Customer;
import io.andrelucas.toiletnearme.customer.CustomerId;
import io.andrelucas.toiletnearme.customer.CustomerNotFound;
import io.andrelucas.toiletnearme.customer.CustomerRepository;
import io.andrelucas.toiletnearme.toilet.business.Toilet;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import io.andrelucas.toiletnearme.toilet.business.usecases.RegisterToiletUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.*;

class RegisterToiletUseCaseTest {
    private ToiletRepository toiletRepository;
    private RegisterToiletUseCase registerToiletUseCase;
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        toiletRepository = mock(ToiletRepository.class);
        customerRepository = mock(CustomerRepository.class);
        registerToiletUseCase = new RegisterToiletUseCase(toiletRepository, customerRepository);
    }

    @Test
    void shouldCreateAToilet() {
        final var captor = ArgumentCaptor.forClass(Toilet.class);
        final var customerId = CustomerId.newId();
        final var customer = new Customer(customerId, "Customer", "customer@Customer.com");
        final var input = new RegisterToiletUseCase.Input("Toilet", 0.0, 0.0, customerId.value());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        registerToiletUseCase.execute(input);

        verify(toiletRepository).save(captor.capture());
        final var toilet = captor.getValue();
        Assertions.assertAll(
                () -> Assertions.assertNotNull(toilet.id()),
                () -> Assertions.assertEquals("Toilet", toilet.name()),
                () -> Assertions.assertEquals(0.0, toilet.geolocation().latitude()),
                () -> Assertions.assertEquals(0.0, toilet.geolocation().longitude())
        );
    }

    @Test
    void shouldNotCreateAToiletAndThrowExceptionWhenDoesNotExistTheCustomer() {
        final var customerId = CustomerId.newId();
        final var input = new RegisterToiletUseCase.Input("Toilet", 0.0, 0.0, customerId.value());

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        Assertions.assertThrows(CustomerNotFound.class, () -> registerToiletUseCase.execute(input));

        verify(toiletRepository, never()).save(Mockito.any());
    }
}