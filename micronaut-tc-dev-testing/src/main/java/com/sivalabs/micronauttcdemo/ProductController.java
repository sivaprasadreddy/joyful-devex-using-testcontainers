package com.sivalabs.micronauttcdemo;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@ExecuteOn(TaskExecutors.IO)
@Controller("/api/products")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Get
    Iterable<Product> list() {
        return productRepository.findAll();
    }

    @Post
    HttpResponse<Product> save(@Body Product product) {
        Product savedProduct = productRepository.save(product);
        return HttpResponse.created(savedProduct);
    }
}
