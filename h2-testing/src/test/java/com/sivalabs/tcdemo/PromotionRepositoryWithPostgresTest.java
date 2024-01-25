package com.sivalabs.tcdemo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


// Using Postgres database via Testcontainers works fine with "ON CONFLICT DO NOTHING".
@DataJpaTest(properties = {
   "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///demodb",
   "spring.test.database.replace=none"
})
class PromotionRepositoryWithPostgresTest {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        promotionRepository.deleteAllInBatch();

        entityManager.persist(new Promotion(null, 1L,BigDecimal.TEN));
        entityManager.persist(new Promotion(null, 2L,BigDecimal.valueOf(24)));
    }

    @Test
    void shouldGetAllProductPromotions() {
        List<Promotion> promotions = promotionRepository.findAll();

        assertThat(promotions).hasSize(2);
    }

    @Test
    void shouldCreateProductIfNotExist() {
        Promotion promotion = new Promotion(null, 1L, BigDecimal.valueOf(25));
        promotionRepository.upsert(promotion);
    }
}