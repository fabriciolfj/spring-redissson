package com.github.fariciolfj.redisspring.service.util;

import com.github.fariciolfj.redisspring.entity.Product;
import com.github.fariciolfj.redisspring.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

//@Service
public class ProductCacheTemplate extends CacheTemplate<Long, Product> {

    @Autowired
    private ProductRepository productRepository;
    private RMapReactive<Long, Product> map;

    public ProductCacheTemplate(final RedissonReactiveClient client) {
        this.map = client.getMap("product", new TypedJsonJacksonCodec(Long.class, Product.class));
    }

    @Override
    protected Mono<Product> getFromSource(final Long id) {
        return productRepository.findById(id);
    }

    @Override
    protected Mono<Product> getFromCache(final Long id) {
        return map.get(id);
    }

    @Override
    protected Mono<Product> updateSource(final Long id, final Product product) {
        return productRepository.findById(id)
                .doOnNext(p -> product.setId(id))
                .flatMap(p -> productRepository.save(product));
    }

    @Override
    protected Mono<Product> updateCache(final Long id, final Product product) {
        return map.fastPut(id, product).thenReturn(product);
    }

    @Override
    protected Mono<Void> deleteFromSource(final Long id) {
        return productRepository.deleteById(id);
    }

    @Override
    protected Mono<Void> deleteFromCache(final Long id) {
        return map.fastRemove(id).then();
    }
}
