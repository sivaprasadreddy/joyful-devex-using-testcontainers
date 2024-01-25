package com.sivalabs.tcdemo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @Modifying
    @Query(
        value = "insert into promotions(product_id, discount) values(:#{#p.productId}, :#{#p.discount}) ON CONFLICT DO NOTHING",
        nativeQuery = true
    )
    void upsert(@Param("p") Promotion promotion);
}
