package com.algaworks.algashop.ordering.infrastructure.adapters.in.web.shoppingcart;

import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.customer.CustomerPersistenceEntityRepository;
import com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart.ShoppingCartPersistenceEntityRepository;
import com.algaworks.algashop.ordering.infrastructure.adapters.in.web.AbstractPresentationIT;
import com.algaworks.algashop.ordering.utils.AlgaShopResourceUtils;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.UUID;

import static com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart.ShoppingCartPersistenceEntityTestDataBuilder.cartWithItems;

public class ShoppingCartControllerIT extends AbstractPresentationIT {

    @Autowired
    private CustomerPersistenceEntityRepository customerRepository;

    @Autowired
    private ShoppingCartPersistenceEntityRepository shoppingCartRepository;

    private static final UUID validCustomerId = UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");
    private static final UUID invalidShoppingCartId = UUID.fromString("a7f4c8b9-2d35-4871-9e3c-4b62f1a9d7e5");
    private static final UUID validShoppingCartId = UUID.fromString("9c8d7e6f-5a4b-3c2d-1e0f-9d8c7b6a5f4e");

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

    @AfterEach
    public void after() {
        wireMockRapidex.stop();
        wireMockProductCatalog.stop();
    }

    /*private void initDatabase() {
        customerRepository.saveAndFlush(
                CustomerPersistenceEntityTestDataBuilder.aCustomer().id(validCustomerId).build()
        );
    }*/

    @Test
    public void shouldCreateShoppingCart() {
        String json = AlgaShopResourceUtils.readContent("json/create-shopping-cart.json");

        UUID createdShoppingCart = RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(json)
                .when()
                    .post("/api/v1/shopping-carts")
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", Matchers.not(Matchers.emptyString()))
                    .extract()
                    .jsonPath().getUUID("id");

        Assertions.assertThat(shoppingCartRepository.existsById(createdShoppingCart)).isTrue();
    }

    @Test
    public void shouldNotCreateAShoppingCartWithoutRequiredField(){
        String json = AlgaShopResourceUtils.readContent("json/create-shopping-cart-without-required-fields.json");
        RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(json)
                .when()
                    .post("/api/v1/shopping-carts")
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                    .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldAddProductToShoppingCart() {
     /*   var shoppingCartPersistence = cartWithItems().items(new HashSet<>())
                .customer(customerRepository.getReferenceById(validCustomerId))
                .build();

        shoppingCartRepository.save(shoppingCartPersistence);

        UUID shoppingCartId = shoppingCartPersistence.getId();*/

        String json = AlgaShopResourceUtils.readContent("json/add-product-to-shopping-cart.json");

        RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
                .when()
                .post("/api/v1/shopping-carts/{shoppingCartId}/items", validShoppingCartId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NO_CONTENT.value());

        var shoppingCartPersistenceEntity = shoppingCartRepository.findById(validShoppingCartId).orElseThrow();
        Assertions.assertThat(shoppingCartPersistenceEntity.getTotalItems()).isEqualTo(4);
    }

    @Test
    public void shouldReturnNotFoundWhenAddingProductToNonexistentShoppingCart() {
        String json = AlgaShopResourceUtils.readContent("json/add-product-to-shopping-cart.json");

        RestAssured
                .given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(json)
                .when()
                     .post("/api/v1/shopping-carts/{shoppingCartId}/items", invalidShoppingCartId)
                .then()
                    .assertThat()
                    .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
