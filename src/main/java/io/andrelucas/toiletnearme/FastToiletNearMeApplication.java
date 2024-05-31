package io.andrelucas.toiletnearme;

import io.andrelucas.toiletnearme.customer.business.Customer;
import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.customer.business.CustomerRepository;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import io.andrelucas.toiletnearme.toilet.business.usecases.RegisterToiletUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FastToiletNearMeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastToiletNearMeApplication.class, args);
    }
}
