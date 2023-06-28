package com.github.fariciolfj.redisspring.service;

import jakarta.annotation.PostConstruct;
import org.redisson.api.BatchOptions;
import org.redisson.api.RBatchReactive;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.LongCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductVisitService {

    @Autowired
    private RedissonReactiveClient client;
    private Sinks.Many<Long> sink;

    public ProductVisitService() {
        this.sink = Sinks.many().unicast().onBackpressureBuffer();
    }

    @PostConstruct
    private void init() {
        this.sink
                .asFlux()
                .buffer(Duration.ofSeconds(3)) //1,
                .map(l -> l.stream().collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                )))
                .flatMap(this::updateBatch)
                .subscribe();
    }

    public void addVisit(final Long id) {
        this.sink.tryEmitNext(id);
    }

    private Mono<Void> updateBatch(final Map<Long, Long> map) {
        final RBatchReactive batch = this.client.createBatch(BatchOptions.defaults());
        final String format = DateTimeFormatter.ofPattern("YYYYMMdd").format(LocalDate.now());
        final RScoredSortedSetReactive<Long> set = batch.getScoredSortedSet("product:visit:" + format, LongCodec.INSTANCE);

        return Flux.fromIterable(map.entrySet())
                        .map(e -> set.addScore(e.getKey(), e.getValue()))
                                .then(batch.execute())
                .then();
    }
}
