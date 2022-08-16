package com.github.fariciolfj.redisspring.controller;

import com.github.fariciolfj.redisspring.city.dto.CityResponse;
import com.github.fariciolfj.redisspring.city.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/city")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping("/{zip}")
    public Mono<CityResponse> find(@PathVariable("zip") final String zip) {
        return cityService.getCity(zip);
    }
}
