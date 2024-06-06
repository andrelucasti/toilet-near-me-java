package io.andrelucas.toiletnearme.toilet.infrastructure.controllers;

import io.andrelucas.toiletnearme.AbstractE2ETest;
import io.andrelucas.toiletnearme.customer.business.CustomerId;
import io.andrelucas.toiletnearme.toilet.business.ToiletId;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;


class ToiletControllerIntegrationTest extends AbstractE2ETest {

    private String toiletBody(final String description,
                              final double latitude,
                              final double longitude,
                              final String customerId) {
        return "{\n" +
                "  \"description\": \"" + description + "\",\n" +
                "  \"latitude\": " + latitude + ",\n" +
                "  \"longitude\": " + longitude + ",\n" +
                "  \"customerId\": \"" + customerId + "\"\n" +
                "}";
    }

    private String customerBody(final String name, final String email) {
        return "{\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"email\": \"" + email + "\"\n" +
                "}";
    }

    private String itemBody(final String value) {
        return "{\n" +
                "  \"description\": \"" + value + "\"\n" +
                "}";
    }

    @Test
    void shouldReturnHttp201WhenAToiletIsCreatedWithSuccess() {
        final var customerId = createNewCustomer();

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(toiletBody("A toilet", 1.0, 1.0, customerId))
                .post("/toilet")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldReturnBadRequestWhenLatitudeIsNotValid() {
        final var customerId = createNewCustomer();

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(toiletBody("A toilet", 91.0, 1.0, customerId))
                .post("/toilet")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldReturnBadRequestWhenLongitudeIsNotValid() {
        final var customerId = createNewCustomer();

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(toiletBody("A toilet", 1.0, 181.0, customerId))
                .post("/toilet")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldReturnBadRequestWhenCustomerIsNotFound() {
        final var customerId = CustomerId.newId();

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(toiletBody("A toilet", 1.0, 1.0, customerId.value()))
                .post("/toilet")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldReturnBadRequestWhenAnItemIsCreatedForAToiletThatNotExists() {
        final var customerId = createNewCustomer();
        final var toiletId = ToiletId.newId().value();
        final var uri = "/toilet/" + toiletId + "/item";

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(toiletBody("A toilet", 1.0, 1.0, customerId))
                .post("/toilet")
                .then()
                .statusCode(201);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(itemBody("Soap"))
                .post(uri)
                .then()
                .statusCode(400);
    }

    @Test
    void shouldAddAnItemWhenToiletAlreadyIsCreated() {
        final var customerId = createNewCustomer();
        MvcResult result = RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(toiletBody("A toilet", 1.0, 1.0, customerId))
                .post("/toilet")
                .mvcResult();

        final var toiletId = Objects.requireNonNull(result.getResponse().getHeader("Location")).split("toilet/")[1];
        Assertions.assertEquals(201, result.getResponse().getStatus());

        final var uri = "/toilet/" + toiletId + "/item";

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(itemBody("Soap"))
                .post(uri)
                .then()
                .statusCode(201);
    }

    private String createNewCustomer() {
        final var name = RandomStringUtils.random(10, true, false);
        final var email = name + "@test.com";

        final var result = RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(customerBody(name, email))
                .post("/customer")
                .getMvcResult();

        final var customerId = Objects.requireNonNull(result.getResponse().getHeader("Location")).split("customer/")[1];
        Assertions.assertEquals(201, result.getResponse().getStatus());

        return customerId;
    }
}