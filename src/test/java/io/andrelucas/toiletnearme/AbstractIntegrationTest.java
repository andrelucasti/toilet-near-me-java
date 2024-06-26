package io.andrelucas.toiletnearme;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.andrelucas.toiletnearme.customer.business.CustomerRepository;
import io.andrelucas.toiletnearme.owner.business.OwnerRepository;
import io.andrelucas.toiletnearme.owner.infrastructure.jpa.OwnerSpringRepository;
import io.andrelucas.toiletnearme.toilet.business.repository.ToiletRepository;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletSpringRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class AbstractIntegrationTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ToiletRepository toiletRepository;

    @Autowired
    protected OwnerRepository ownerRepository;

    @Autowired
    protected CustomerRepository customerRepository;

    @Autowired
    protected ToiletOutboxSpringRepository toiletOutboxSpringRepository;

    @Autowired
    private ClearData clearData;

    @BeforeEach
    void setUp() {
      clearData.clear();
    }
}
