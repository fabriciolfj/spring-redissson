package com.github.fariciolfj.redisspring.geo.controller;

import com.github.fariciolfj.redisspring.geo.dto.Restaurant;
import com.github.fariciolfj.redisspring.geo.service.RestaurantLocatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RequestMapping("/geo")
@RestController
public class RestaurantGeoController {

    private final RestaurantLocatorService service;

    @GetMapping("/{zip}")
    public Flux<Restaurant> find(@PathVariable("zip") final String zip) {
        return service.findRestaurants(zip);
    }
}
