package com.github.fariciolfj.redisspring.service.util;

import com.github.fariciolfj.redisspring.entity.Product;
import com.github.fariciolfj.redisspring.repository.ProductRepository;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductLocalCacheTemplate extends CacheTemplate<Long, Product> {

    @Autowired
    private ProductRepository productRepository;
    private RLocalCachedMap<Long, Product> map;

    public ProductLocalCacheTemplate(final RedissonClient client) {
        final LocalCachedMapOptions<Long, Product> mapOptions = LocalCachedMapOptions.<Long, Product>defaults()
                .reconnectionStrategy(LocalCachedMapOptions.ReconnectionStrategy.CLEAR)
                .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE);

        this.map = client.getLocalCachedMap("product", new TypedJsonJacksonCodec(Long.class, Product.class), mapOptions);
    }

    @Override
    protected Mono<Product> getFromSource(final Long id) {
        return productRepository.findById(id);
    }

    @Override
    protected Mono<Product> getFromCache(final Long id) {
        return Mono.justOrEmpty(map.get(id));
    }

    @Override
    protected Mono<Product> updateSource(final Long id, final Product product) {
        return productRepository.findById(id)
                .doOnNext(p -> product.setId(id))
                .flatMap(p -> productRepository.save(product));
    }

    @Override
    protected Mono<Product> updateCache(final Long id, final Product product) {
        return Mono.create(sink ->
            map.fastPutAsync(id, product)
                    .thenAccept(b -> sink.success(product))
                    .exceptionally(e -> {
                        sink.error(e);
                        return null;
                    }));
    }

    @Override
    protected Mono<Void> deleteFromSource(final Long id) {
        return productRepository.deleteById(id);
    }

    @Override
    protected Mono<Void> deleteFromCache(final Long id) {
        return Mono.create(sink ->
                map.removeAsync(id)
                        .thenAccept(b -> sink.success())
                        .exceptionally(e -> {
                            sink.error(e);
                            return null;
                        }));
    }
}
