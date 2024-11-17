package com.sivalabs.qurkustcdemo;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
class ProductControllerTests {
    @Inject
    ProductService productService;

    List<Product> products = List.of(
            new Product(null, "P100", "Product 1", "Product 1 desc",  BigDecimal.TEN),
            new Product(null, "P101", "Product 2", "Product 2 desc", BigDecimal.valueOf(24)));

    @BeforeEach
    void setUp() {
        productService.deleteAll();
        productService.saveAll(products);
    }

    @Test
    void shouldGetProducts() {
        given().when()
                .get("/api/products")
                .then()
                .statusCode(200)
                .body("size()", equalTo(products.size()));
    }

}