package com.github.fariciolfj.redisspring.controller;

import com.github.fariciolfj.redisspring.entity.Product;
import com.github.fariciolfj.redisspring.service.ProductServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/products")
public class ProductControllerV2 {

    private final ProductServiceV2 productService;

    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable("id") final Long id) {
        return productService.getProduct(id);
    }

    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable("id") final Long id, @RequestBody final Mono<Product> productMono) {
        return productService.updateProduct(id, productMono);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteProduct(@PathVariable("id") final Long id) {
        return productService.deleteProduct(id);
    }
}
