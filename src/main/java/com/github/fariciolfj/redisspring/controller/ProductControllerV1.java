package com.github.fariciolfj.redisspring.controller;

import com.github.fariciolfj.redisspring.entity.Product;
import com.github.fariciolfj.redisspring.service.ProductServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductControllerV1 {

    private final ProductServiceV1 productService;

    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable("id") final Long id) {
        return productService.getProduct(id);
    }

    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable("id") final Long id, @RequestBody final Mono<Product> productMono) {
        return productService.updateProduct(id, productMono);
    }
}
