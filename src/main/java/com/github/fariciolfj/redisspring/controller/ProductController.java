package com.github.fariciolfj.redisspring.controller;

import com.github.fariciolfj.redisspring.entity.Product;
import com.github.fariciolfj.redisspring.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public Mono<Product> getProduct(@PathVariable("id") final Long id) {
        return productService.getProduct(id);
    }

    @PutMapping("/{id}")
    public Mono<Product> updateProduct(@PathVariable("id") final Long id, @RequestBody final Mono<Product> productMono) {
        return productService.updateProduct(id, productMono);
    }
}
