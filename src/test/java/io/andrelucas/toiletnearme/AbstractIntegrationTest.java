package io.andrelucas.toiletnearme;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.andrelucas.toiletnearme.customer.CustomerRepository;
import io.andrelucas.toiletnearme.owner.business.OwnerRepository;
import io.andrelucas.toiletnearme.owner.infrastructure.jpa.OwnerSpringRepository;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletSpringRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AbstractIntegrationTest {

    @Autowired
    private ToiletSpringRepository toiletSpringRepository;
    @Autowired
    private ToiletOutboxSpringRepository toiletOutboxSpringRepository;
    @Autowired
    private OwnerSpringRepository ownerSpringRepository;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ToiletRepository toiletRepository;

    @Autowired
    protected OwnerRepository ownerRepository;

    @Autowired
    protected CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        ownerSpringRepository.deleteAll();
        toiletSpringRepository.deleteAll();
        toiletOutboxSpringRepository.deleteAll();
    }
}
