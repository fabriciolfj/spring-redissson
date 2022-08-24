package com.github.fariciolfj.redisspring.controller;

import com.github.fariciolfj.redisspring.service.BusinessMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("product/metrics")
public class BusinessMetricsController {

    private final BusinessMetricsService businessMetricsService;
    
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<Long, Double>> getMetrics() {
        return this.businessMetricsService.top3Products()
                .repeatWhen(l -> Flux.interval(Duration.ofSeconds(3)));
    }
}
