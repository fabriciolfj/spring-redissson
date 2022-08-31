package com.github.fariciolfj.redisspring.geo.service;

import com.github.fariciolfj.redisspring.geo.dto.GeoLocation;
import com.github.fariciolfj.redisspring.geo.dto.Restaurant;
import org.redisson.api.GeoUnit;
import org.redisson.api.RGeoReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Service
public class RestaurantLocatorService {

    private RMapReactive<String, GeoLocation> map;
    private RGeoReactive<Restaurant> geo;

    public RestaurantLocatorService(final RedissonReactiveClient client) {
        map = client.getMap("usa", new TypedJsonJacksonCodec(String.class, GeoLocation.class));
        geo = client.getGeo("restaurants", new TypedJsonJacksonCodec(Restaurant.class));
    }

    public Flux<Restaurant> findRestaurants(final String zip) {
        return map.get(zip)
                .map(r -> GeoSearchArgs.from(r.getLongitude(), r.getLatitude()).radius(5, GeoUnit.MILES))
                .flatMap(g -> geo.search(g))
                .flatMapIterable(Function.identity());
    }
}
