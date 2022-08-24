package com.github.fariciolfj.redisspring.service;

import com.github.fariciolfj.redisspring.entity.Product;
import com.github.fariciolfj.redisspring.service.util.ProductCacheTemplate;
import com.github.fariciolfj.redisspring.service.util.ProductLocalCacheTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceV2 {

    /*@Autowired
    private ProductCacheTemplate productCacheTemplate;*/

    @Autowired
    private ProductLocalCacheTemplate productCacheTemplate;

    @Autowired
    private ProductVisitService productVisitService;

    public Mono<Product> getProduct(final Long id) {
        return productCacheTemplate.get(id)
                .doFirst(() -> this.productVisitService.addVisit(id));
    }

    public Mono<Product> updateProduct(final Long id, final Mono<Product> productMono) {
        return productMono.flatMap(p -> productCacheTemplate.update(id, p));
    }

    public Mono<Void> deleteProduct(final Long id) {
        return productCacheTemplate.delete(id);
    }
}
