package com.github.fariciolfj.redisspring.controller;

import com.github.fariciolfj.redisspring.fib.FibService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/fib")
@RequiredArgsConstructor
public class FibController {

    private final FibService fibService;

    @GetMapping("/{index}")
    public Mono<Integer> get(@PathVariable("index") final int index) {
        return Mono.fromSupplier(() -> fibService.getLib(index));
    }

    @PutMapping("/{index}")
    public Mono<Void> put(@PathVariable("index") final int index) {
        return Mono.fromRunnable(() -> fibService.clearCache(index));
    }

}
