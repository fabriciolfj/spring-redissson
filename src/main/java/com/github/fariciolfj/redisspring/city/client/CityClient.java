package com.github.fariciolfj.redisspring.city.client;

import com.github.fariciolfj.redisspring.city.dto.CityResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CityClient {

    private final WebClient webClient;

    public CityClient(@Value("${city.service.url}") final String url) {
        this.webClient = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    public Mono<CityResponse> getCity(final String zipCode) {
        return webClient.get()
                .uri("/{zipCode}", zipCode)
                .retrieve()
                .bodyToMono(CityResponse.class)
                .doOnSuccess(c -> System.out.println("consumo com sucesso o servico city"));
    }

    public Flux<CityResponse> findAll() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(CityResponse.class);
    }
}
