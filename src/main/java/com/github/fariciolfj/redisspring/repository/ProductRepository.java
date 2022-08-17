package com.github.fariciolfj.redisspring.repository;

import com.github.fariciolfj.redisspring.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
}
