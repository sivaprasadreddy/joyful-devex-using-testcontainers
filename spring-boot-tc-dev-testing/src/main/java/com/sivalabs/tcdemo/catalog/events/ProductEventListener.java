package com.sivalabs.tcdemo.catalog.events;

import com.sivalabs.tcdemo.catalog.domain.Product;
import com.sivalabs.tcdemo.catalog.domain.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener {
    private static final Logger log = LoggerFactory.getLogger(ProductEventListener.class);

    private final ProductService productService;

    public ProductEventListener(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "${app.topic}")
    public void handleProductCreatedEvent(Product product) {
        log.info("Product event received from products topic");
        productService.create(product);
    }
}
