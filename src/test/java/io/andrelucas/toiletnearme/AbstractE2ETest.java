package io.andrelucas.toiletnearme;

import io.andrelucas.toiletnearme.customer.infrastructure.jpa.CustomerSpringRepository;
import io.andrelucas.toiletnearme.owner.infrastructure.jpa.OwnerSpringRepository;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletSpringRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractE2ETest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ClearData clearData;

    @BeforeEach
    void setUp() {
        clearData.clear();

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }
}
