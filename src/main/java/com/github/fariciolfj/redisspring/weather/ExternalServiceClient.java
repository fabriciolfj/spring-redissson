package com.github.fariciolfj.redisspring.weather;

import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class ExternalServiceClient {

    @CachePut(value = "weather", key = "#zip")
    public int getValue(int zip) {
        return ThreadLocalRandom.current().nextInt(60, 100);
    }
}
