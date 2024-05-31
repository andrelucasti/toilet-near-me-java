package io.andrelucas.toiletnearme.toilet.infrastructure.controllers;

import io.andrelucas.toiletnearme.AbstractE2ETest;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


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

    @Test
    void shouldReturnHttp201WhenAToiletIsCreatedWithSuccess() {
        final var result = RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(customerBody("André Lucas", "andrelucastic@gmail.com"))
                .post("/customer")
                .getMvcResult();

        final var customerId = result.getResponse().getHeader("Location").split("customer/")[1];

        Assertions.assertEquals(201, result.getResponse().getStatus());


        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(toiletBody("A toilet", 1.0, 1.0, customerId))
                .post("/toilet")
                .then()
                .statusCode(201);
    }
}