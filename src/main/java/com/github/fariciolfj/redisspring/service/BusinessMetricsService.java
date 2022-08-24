package com.github.fariciolfj.redisspring.service;

import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BusinessMetricsService {

    @Autowired
    private RedissonReactiveClient client;

    public Mono<Map<Long, Double>> top3Products() {
        final String format = DateTimeFormatter.ofPattern("YYYYMMdd").format(LocalDate.now());
        final RScoredSortedSetReactive<Long> set = client.getScoredSortedSet("product:visit:" + format, LongCodec.INSTANCE);

        return set.entryRangeReversed(0, 2)
                .map(list -> list.stream().collect(Collectors.toMap(
                        ScoredEntry::getValue,
                        ScoredEntry::getScore,
                        (a ,b) -> a,
                        LinkedHashMap::new
                )));
    }
}
