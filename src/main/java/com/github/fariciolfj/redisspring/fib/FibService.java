package com.github.fariciolfj.redisspring.fib;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FibService {

    private int total;

    @Cacheable(value = "math:fib", key = "#index")
    public int getLib(final int index) {
        log.info("Calcular fib: {}", index);
        total = 0;
        return calc(index);
    }

    // put, post, path, delete
    @CacheEvict(value = "math:fib", key = "#index")
    public void clearCache(int index) {
        System.out.println("clearing hash key");
    }

    private int calc(final int index) {
        if (index < 2) {
            total += index;
            log.info("Somando: {}", total);
            return index;
        }

        return calc(index -1) + calc(index - 2);
    }
}
