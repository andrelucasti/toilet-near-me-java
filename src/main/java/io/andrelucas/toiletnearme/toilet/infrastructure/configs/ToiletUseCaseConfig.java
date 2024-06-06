package io.andrelucas.toiletnearme.toilet.infrastructure.configs;

import io.andrelucas.toiletnearme.customer.business.CustomerRepository;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import io.andrelucas.toiletnearme.toilet.business.usecases.AddNewItemUseCase;
import io.andrelucas.toiletnearme.toilet.business.usecases.RegisterToiletUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToiletUseCaseConfig {

    private final ToiletRepository toiReRepository;
    private final CustomerRepository customerRepository;

    public ToiletUseCaseConfig(final CustomerRepository customerRepository,
                               final ToiletRepository toiReRepository) {
        this.customerRepository = customerRepository;
        this.toiReRepository = toiReRepository;
    }

    @Bean
    public RegisterToiletUseCase registerToiletUseCase() {
        return new RegisterToiletUseCase(toiReRepository, customerRepository);
    }

    @Bean
    public AddNewItemUseCase addNewItemUseCase() {
        return new AddNewItemUseCase();
    }
}
