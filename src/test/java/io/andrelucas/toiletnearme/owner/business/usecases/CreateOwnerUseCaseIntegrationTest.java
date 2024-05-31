package io.andrelucas.toiletnearme.owner.business.usecases;

import io.andrelucas.toiletnearme.AbstractIntegrationTest;
import io.andrelucas.toiletnearme.customer.business.Customer;
import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.customer.business.CustomerRepository;
import io.andrelucas.toiletnearme.owner.business.Owner;
import io.andrelucas.toiletnearme.owner.business.OwnerAlreadyExistsException;
import io.andrelucas.toiletnearme.owner.business.OwnerRepository;
import io.andrelucas.toiletnearme.toilet.business.Geolocation;
import io.andrelucas.toiletnearme.toilet.business.Toilet;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CreateOwnerUseCaseIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ToiletRepository toiletRepository;

    @Autowired
    private OwnerRepository ownerRepository;


    @Test
    void shouldCreateOwner() {
        final var createOwnerUseCase = new CreateOwnerUseCase(customerRepository, toiletRepository, ownerRepository);
        final var customer = new Customer(CustomerId.newId(), "andre", "andre@gmail.com");
        customerRepository.save(customer);

        final var toilet = new Toilet(ToiletId.newId(), "toilet", new Geolocation(0.0, 0.0));
        toiletRepository.save(toilet);

        final var input = new CreateOwnerUseCase.Input(customer.id().value(), toilet.id().value());
        createOwnerUseCase.execute(input);

        ownerRepository.findAll().stream()
                .findFirst()
                .ifPresentOrElse(owner -> {
                    assertThat(owner.customerId()).isEqualTo(customer.id());
                    assertThat(owner.toiletId()).isEqualTo(toilet.id());
                }, () -> {
                    Assertions.fail("Owner not found");
                });

    }

    @Test
    void shouldBeOwnerMoreThanOneToilet() {
        final var createOwnerUseCase = new CreateOwnerUseCase(customerRepository, toiletRepository, ownerRepository);
        final var customer = new Customer(CustomerId.newId(), "andre", "andre@gmail.com");
        customerRepository.save(customer);

        final var toilet = new Toilet(ToiletId.newId(), "toilet", new Geolocation(0.0, 0.0));
        toiletRepository.save(toilet);

        final var input = new CreateOwnerUseCase.Input(customer.id().value(), toilet.id().value());
        createOwnerUseCase.execute(input);

        final var toilet2 = new Toilet(ToiletId.newId(), "toilet2", new Geolocation(0.0, 0.0));
        toiletRepository.save(toilet2);

        final var input2 = new CreateOwnerUseCase.Input(customer.id().value(), toilet2.id().value());
        createOwnerUseCase.execute(input2);

        final var ownerList = ownerRepository.findAll();

        assertThat(ownerList.size()).isEqualTo(2);

        final var toiletIds = ownerList.stream()
                .map(Owner::toiletId)
                .toList();

        final var customerIds = ownerList.stream()
                .map(Owner::customerId)
                .toList();

        assertThat(toiletIds).containsOnly(toilet.id(), toilet2.id());
        assertThat(customerIds).containsOnly(customer.id());
    }

    @Test
    void shouldNotBeOwnershipWithMoreThanAnOwner() {
        final var createOwnerUseCase = new CreateOwnerUseCase(customerRepository, toiletRepository, ownerRepository);

        final var toilet = new Toilet(ToiletId.newId(), "toilet", new Geolocation(0.0, 0.0));
        toiletRepository.save(toilet);

        final var customer1 = new Customer(CustomerId.newId(), "andre", "andre@gmail.com");
        customerRepository.save(customer1);

        final var customer2 = new Customer(CustomerId.newId(), "lucas", "lucas@gmail.com");
        customerRepository.save(customer2);


        final var input = new CreateOwnerUseCase.Input(customer1.id().value(), toilet.id().value());
        createOwnerUseCase.execute(input);

        final var input2 = new CreateOwnerUseCase.Input(customer2.id().value(), toilet.id().value());

        assertThrows(OwnerAlreadyExistsException.class, () -> createOwnerUseCase.execute(input2));

        final var ownerList = ownerRepository.findAll();

        assertThat(ownerList.size()).isEqualTo(1);

        final var toiletIds = ownerList.stream()
                .map(Owner::toiletId)
                .toList();

        final var customerIds = ownerList.stream()
                .map(Owner::customerId)
                .toList();

        assertThat(toiletIds).containsOnly(toilet.id());
        assertThat(customerIds).containsOnly(customer1.id());
    }
}