package com.sivalabs.tcdemo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class PromotionRepositoryWithH2Test {

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
        //promotionRepository.upsert(promotion);

        assertThatThrownBy(() -> promotionRepository.upsert(promotion))
                .isInstanceOf(DataAccessException.class);
    }
}