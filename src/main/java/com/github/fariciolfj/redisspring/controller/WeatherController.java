package com.github.fariciolfj.redisspring.controller;

import com.github.fariciolfj.redisspring.weather.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/{value}")
    public Mono<Integer> find(@PathVariable("value") int value) {
        return Mono.fromSupplier(() -> weatherService.getWeather(value));
    }
}
