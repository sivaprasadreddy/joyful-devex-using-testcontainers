package com.sivalabs.tcdemo.catalog.api;

import com.sivalabs.tcdemo.catalog.domain.Product;
import com.sivalabs.tcdemo.catalog.domain.ProductService;
import com.sivalabs.tcdemo.catalog.events.ProductEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
class ProductController {
    private final ProductService productService;
    private final ProductEventPublisher productEventPublisher;

    ProductController(ProductService productService,
                      ProductEventPublisher productEventPublisher) {
        this.productService = productService;
        this.productEventPublisher = productEventPublisher;
    }

    @GetMapping
    List<Product> getAll() {
        return productService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void create(@RequestBody Product product) {
        productEventPublisher.publishProductCreatedEvent(product);
    }
}
