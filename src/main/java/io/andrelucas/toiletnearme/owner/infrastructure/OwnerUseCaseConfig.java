package io.andrelucas.toiletnearme.owner.infrastructure;


import io.andrelucas.toiletnearme.customer.business.CustomerRepository;
import io.andrelucas.toiletnearme.owner.business.OwnerRepository;
import io.andrelucas.toiletnearme.owner.business.usecases.CreateOwnerUseCase;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OwnerUseCaseConfig {

    private final CustomerRepository customerRepository;
    private final ToiletRepository toiletRepository;
    private final OwnerRepository ownerRepository;

    public OwnerUseCaseConfig(final CustomerRepository customerRepository,
                              final ToiletRepository toiletRepository,
                              final OwnerRepository ownerRepository) {

        this.customerRepository = customerRepository;
        this.toiletRepository = toiletRepository;
        this.ownerRepository = ownerRepository;
    }

    @Bean
    public CreateOwnerUseCase createOwnerUseCase(){
        return new CreateOwnerUseCase(customerRepository, toiletRepository, ownerRepository);
    }
}
