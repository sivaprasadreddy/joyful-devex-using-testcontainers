package com.sivalabs.micronauttcdemo;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.micronaut.http.HttpStatus.CREATED;
import static io.micronaut.http.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class ProductControllerTest {
    private BlockingHttpClient blockingClient;

    @Inject
    @Client("/")
    HttpClient client;

    @BeforeEach
    void setup() {
        blockingClient = client.toBlocking();
    }
    @Test
    void shouldGetAllProducts() {
        HttpRequest<?> request = HttpRequest.GET("/api/products");
        HttpResponse<?> response = blockingClient.exchange(request);

        assertEquals(OK, response.getStatus());
    }
    @Test
    void shouldCreateTodo() {
        HttpRequest<?> request = HttpRequest.POST("/api/products",
                new Product(null, "P999", "Product 999", "Product 999 Desc", BigDecimal.TEN));
        HttpResponse<?> response = blockingClient.exchange(request);

        assertEquals(CREATED, response.getStatus());
    }


}