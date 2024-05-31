package io.andrelucas.toiletnearme.owner.business.usecases;

import io.andrelucas.toiletnearme.customer.business.Customer;
import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.customer.business.CustomerNotFound;
import io.andrelucas.toiletnearme.customer.business.CustomerRepository;
import io.andrelucas.toiletnearme.owner.business.Owner;
import io.andrelucas.toiletnearme.owner.business.OwnerRepository;
import io.andrelucas.toiletnearme.toilet.business.*;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateOwnerUseCaseTest {
    private final ToiletRepository toiletRepository = mock(ToiletRepository.class);
    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final OwnerRepository ownerRepository = mock(OwnerRepository.class);

    private CreateOwnerUseCase subject;

    @BeforeEach
    void setUp() {
        reset(toiletRepository, customerRepository, ownerRepository);
        subject = new CreateOwnerUseCase(customerRepository, toiletRepository, ownerRepository);
    }

    @Test
    void shouldThrowExceptionWhenToiletIsNotFound() {
        final var customerId = UUID.randomUUID().toString();
        final var toiletId = UUID.randomUUID().toString();
        final var customer = new Customer(CustomerId.with(customerId), "CustomerZin", "customer@gmail.com");

        when(customerRepository.findById(CustomerId.with(customerId))).thenReturn(Optional.of(customer));
        when(toiletRepository.findById(ToiletId.with(toiletId))).thenReturn(Optional.empty());

        // when
        var input = new CreateOwnerUseCase.Input(customerId, toiletId);
        // then
        assertThrows(ToiletNotFoundException.class, () -> subject.execute(input));
    }

    @Test
    void shouldThrowExceptionWhenCustomerIsNotFound() {
        final var customerId = UUID.randomUUID().toString();
        final var toiletId = UUID.randomUUID().toString();
        final var toilet = new Toilet(ToiletId.with(toiletId), "ToiletZin", new Geolocation(0, 0));

        when(toiletRepository.findById(ToiletId.with(toiletId))).thenReturn(Optional.of(toilet));
        when(customerRepository.findById(CustomerId.with(customerId))).thenReturn(Optional.empty());

        var input = new CreateOwnerUseCase.Input(customerId, toiletId);

        assertThrows(CustomerNotFound.class, () -> subject.execute(input));
    }

    @Test
    void shouldCreateOwner() {
        final var captor = ArgumentCaptor.forClass(Owner.class);
        final var customerId = UUID.randomUUID().toString();
        final var toiletId = UUID.randomUUID().toString();
        final var toilet = new Toilet(ToiletId.with(toiletId), "ToiletZin", new Geolocation(0, 0));
        final var customer = new Customer(CustomerId.with(customerId), "CustomerZin", "customer@gmail.com");

        when(toiletRepository.findById(ToiletId.with(toiletId))).thenReturn(Optional.of(toilet));
        when(customerRepository.findById(CustomerId.with(customerId))).thenReturn(Optional.of(customer));

        var input = new CreateOwnerUseCase.Input(customerId, toiletId);

        subject.execute(input);

        verify(ownerRepository, times(1)).save(captor.capture());

        Assertions.assertAll(
                () -> assertEquals(customerId, captor.getValue().customerId().value()),
                () -> assertEquals(toiletId, captor.getValue().toiletId().value())
        );
    }
}