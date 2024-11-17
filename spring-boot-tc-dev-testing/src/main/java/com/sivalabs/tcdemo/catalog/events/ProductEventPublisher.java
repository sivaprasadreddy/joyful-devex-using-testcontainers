package com.sivalabs.tcdemo.catalog.events;

import com.sivalabs.tcdemo.ApplicationProperties;
import com.sivalabs.tcdemo.catalog.domain.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(ProductEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ApplicationProperties properties;

    public ProductEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
                                 ApplicationProperties properties) {
        this.kafkaTemplate = kafkaTemplate;
        this.properties = properties;
    }

    public void publishProductCreatedEvent(Product product) {
        kafkaTemplate.send(properties.topic(), product);
        log.info("Product event sent to products topic");
    }
}
