package io.andrelucas.toiletnearme.customer.infrastructure.config;

import io.andrelucas.toiletnearme.customer.business.CustomerRepository;
import io.andrelucas.toiletnearme.customer.business.usecases.CreateCustomerUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerUseCaseConfig {

    private final CustomerRepository customerRepository;

    public CustomerUseCaseConfig(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Bean
    public CreateCustomerUseCase createCustomerUseCase() {
        return new CreateCustomerUseCase(customerRepository);
    }
}
