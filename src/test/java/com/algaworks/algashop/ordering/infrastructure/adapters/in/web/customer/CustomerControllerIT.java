package com.algaworks.algashop.ordering.infrastructure.adapters.in.web.customer;

import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityRepository;
import com.algaworks.algashop.ordering.infrastructure.adapters.in.web.AbstractPresentationIT;
import com.algaworks.algashop.ordering.utils.AlgaShopResourceUtils;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.UUID;

public class CustomerControllerIT extends AbstractPresentationIT {

    @Autowired
    private CustomerPersistenceEntityRepository customerRepository;

    private static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");

    private static final UUID invalidCustomerId = UUID.fromString("b7f6a8b9-2d35-4871-9e3c-4b62f1a9d7e5");

    @BeforeEach
    public void setup() {
        super.beforeEach();
    }

    @BeforeAll
    public static void setupBeforeAll() {
        AbstractPresentationIT.initWireMock();
    }

    @AfterAll
    public static void afterAll() {
        AbstractPresentationIT.stopMock();
    }

    @Test
    public void shouldCreateCustomer() {
        String json = AlgaShopResourceUtils.readContent("json/create-customer.json");

        UUID createdCustomerId = RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(json)
                .when()
                    .post("/api/v1/customers")
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", Matchers.not(Matchers.emptyString()))
                    .extract()
                    .jsonPath().getUUID("id");

        Assertions.assertThat(customerRepository.existsById(createdCustomerId)).isTrue();
    }

    @Test
    public void shouldFailToCreateCustomerWithMissingFields(){
        String json = AlgaShopResourceUtils.readContent("json/create-customer-without-required-fields.json");
        RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(json)
                .when()
                     .post("/api/v1/customers")
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("title", Matchers.notNullValue())
                    .body("status", Matchers.equalTo(400))
                    .body("fields", Matchers.notNullValue())
                    .body("fields.email", Matchers.equalTo("must not be blank"));
    }

    @Test
    public void shouldArchiveCustomer() {
        RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .delete("/api/v1/customers/{customerId}", validCustomerId)
                .then()
                     .assertThat()
                     .statusCode(HttpStatus.NO_CONTENT.value());

        Assertions.assertThat(customerRepository.existsById(validCustomerId)).isTrue();
        Assertions.assertThat(customerRepository.findById(validCustomerId).orElseThrow().getArchived()).isTrue();
    }

    @Test
    public void shouldTryArchiveACustomerNotExisting(){
        RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .delete("/api/v1/customers/"+invalidCustomerId)
                .then()
                     .assertThat()
                    .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
