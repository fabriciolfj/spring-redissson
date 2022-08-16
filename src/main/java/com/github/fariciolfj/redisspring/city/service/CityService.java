package com.github.fariciolfj.redisspring.city.service;

import com.github.fariciolfj.redisspring.city.client.CityClient;
import com.github.fariciolfj.redisspring.city.dto.CityResponse;
import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityClient client;


    private RMapCacheReactive<String, CityResponse> cityMap;

    public CityService(final RedissonReactiveClient client) {
        this.cityMap = client.getMapCache("city", new TypedJsonJacksonCodec(String.class, CityResponse.class));
    }

    public Mono<CityResponse> getCity(final String zipCode) {
        return cityMap.get(zipCode)
                .switchIfEmpty(client.getCity(zipCode));
               /* .switchIfEmpty(client.getCity(zipCode)
                        .doOnSuccess(c -> System.out.printf("Efetuado consumo com sucesso ao client city"))
                        .flatMap(c -> cityMap.fastPut(zipCode, c, 10, TimeUnit.SECONDS).thenReturn(c)));*/
    }

    @Scheduled(fixedRate = 10_000)
    public void updateCity() {
        this.client.findAll()
                .collectList()
                .map(list -> list.stream().collect(Collectors.toMap(CityResponse::getZip, Function.identity())))
                .flatMap(this.cityMap::putAll)
                .subscribe();
    }
}
