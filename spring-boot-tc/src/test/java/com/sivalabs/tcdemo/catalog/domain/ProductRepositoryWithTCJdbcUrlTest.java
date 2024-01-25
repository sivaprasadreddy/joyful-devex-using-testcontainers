package com.sivalabs.tcdemo.catalog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///demodb"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryWithTCJdbcUrlTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        entityManager.persist(new Product(null, "P11","pname1", "pdescr1", BigDecimal.TEN));
        entityManager.persist(new Product(null, "P12","pname2", "pdescr2", BigDecimal.TEN));
    }

    @Test
    void shouldGetAllProducts() {
        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(2);
    }
}