package com.github.fariciolfj.redisspring.geo.service;

import com.github.fariciolfj.redisspring.geo.dto.GeoLocation;
import com.github.fariciolfj.redisspring.geo.dto.Restaurant;
import com.github.fariciolfj.redisspring.geo.util.RestaurantUtil;
import org.redisson.api.RGeoReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DataSetupRestaurantsService implements CommandLineRunner {

    @Autowired
    private RedissonReactiveClient client;
    private RGeoReactive<Restaurant> geo;
    private RMapReactive<String, GeoLocation> map;

    @Override
    public void run(String... args) throws Exception {
        geo = client.getGeo("restaurants", new TypedJsonJacksonCodec(Restaurant.class));
        map = client.getMap("usa", new TypedJsonJacksonCodec(String.class, GeoLocation.class));

        Flux.fromIterable(RestaurantUtil.getRestaurants())
                .flatMap(r -> this.geo.add(r.getLongitude(), r.getLatitude(), r).thenReturn(r))
                .flatMap(g -> this.map.fastPut(g.getZip(), GeoLocation.of(g.getLongitude(), g.getLatitude())))
                .doFinally(s -> System.out.println("add restaurants " + s))
                .subscribe();
    }
}
