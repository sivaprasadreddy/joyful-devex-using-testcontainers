package com.sivalabs.qurkustcdemo;


import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
@Transactional
public class ProductService {

    public List<Product> getAll() {
        return Product.findAll(Sort.ascending("name")).list();
    }

    public void create(Product product) {
        product.setId(null);
        product.persist();
    }

    public void deleteAll() {
        Product.deleteAll();
    }

    public void saveAll(List<Product> products) {
        Product.persist(products);
    }
}
