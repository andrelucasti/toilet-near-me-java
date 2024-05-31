package io.andrelucas.toiletnearme;

import io.andrelucas.toiletnearme.customer.infrastructure.jpa.CustomerSpringRepository;
import io.andrelucas.toiletnearme.owner.infrastructure.jpa.OwnerSpringRepository;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletSpringRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractE2ETest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private CustomerSpringRepository customerSpringRepository;
    @Autowired
    private ToiletOutboxSpringRepository toiletOutboxSpringRepository;
    @Autowired
    private ToiletSpringRepository toiletSpringRepository;
    @Autowired
    private OwnerSpringRepository ownerSpringRepository;

    @BeforeEach
    void setUp() {
        ownerSpringRepository.deleteAll();
        customerSpringRepository.deleteAll();
        toiletSpringRepository.deleteAll();
        toiletOutboxSpringRepository.deleteAll();

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }
}
