package io.andrelucas.toiletnearme;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.andrelucas.toiletnearme.customer.Customer;
import io.andrelucas.toiletnearme.customer.CustomerId;
import io.andrelucas.toiletnearme.customer.CustomerRepository;
import io.andrelucas.toiletnearme.customer.infrastructure.jpa.CustomerRepositoryJPAImpl;
import io.andrelucas.toiletnearme.customer.infrastructure.jpa.CustomerSpringRepository;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import io.andrelucas.toiletnearme.toilet.business.usecases.RegisterToiletUseCase;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletRepositoryJPAImpl;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletSpringRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FastToiletNearMeApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(FastToiletNearMeApplication.class, args);
    }

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ToiletRepository toiletRepository;


    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello World!");

        final var customerId = CustomerId.newId();
        customerRepository.save(new Customer(customerId, "GG", "GG"));
        RegisterToiletUseCase.Input input = new RegisterToiletUseCase.Input("GG", 0, 0, customerId.value());

        RegisterToiletUseCase registerToiletUseCase = new RegisterToiletUseCase(toiletRepository, customerRepository);
        registerToiletUseCase.execute(input);
    }
}
