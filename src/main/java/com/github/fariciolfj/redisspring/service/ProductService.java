package com.github.fariciolfj.redisspring.service;

import com.github.fariciolfj.redisspring.entity.Product;
import com.github.fariciolfj.redisspring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Mono<Product> getProduct(final Long id) {
        return repository.findById(id);
    }

    public Mono<Product> updateProduct(final Long id, final Mono<Product> productMono) {
        return repository.findById(id)
                .flatMap(p -> productMono.doOnNext(pr -> pr.setId(id)))
                .flatMap(repository::save);
    }
}
