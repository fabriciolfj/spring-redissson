package com.github.fariciolfj.redisspring.service;

import com.github.fariciolfj.redisspring.entity.Product;
import com.github.fariciolfj.redisspring.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;


@Service
@Slf4j
public class DataSetupService implements CommandLineRunner {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    @Value("classpath:schema.sql")
    private Resource resource;

    @Override
    public void run(String... args) throws Exception {
        final var sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        log.info(sql);

        var insert = Flux.range(1,1000)
                                    .map(i -> Product.builder()
                                            .id(null)
                                            .description("product" + i)
                                            .price(BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(1, 100)))
                                            .build())
                                    .collectList()
                                    .flatMapMany(l -> repository.saveAll(l))
                                    .then();

        entityTemplate.getDatabaseClient()
                .sql(sql)
                .then()
                .then(insert)
                .doFinally(s -> log.info("data setup done " + s))
                .subscribe();
    }
}
