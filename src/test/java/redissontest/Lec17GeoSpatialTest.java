package redissontest;

import org.junit.jupiter.api.Test;
import org.redisson.api.GeoUnit;
import org.redisson.api.RGeoReactive;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import redissontest.dto.Restaurant;
import redissontest.util.RestaurantUtil;

import java.util.function.Function;

public class Lec17GeoSpatialTest extends BaseTest {

    @Test
    public void add() {
        var test = RestaurantUtil.getRestaurants();
        final RGeoReactive<Restaurant> geo = this.redisClient.getGeo("restaurants", new TypedJsonJacksonCodec(Restaurant.class));
        Mono<Void> mono = Flux.fromIterable(test)
                .flatMap(c -> geo.add(c.getLongitude(), c.getLatitude(), c))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        var radius = GeoSearchArgs.from(-96.80539, 32.78136)
                .radius(3, GeoUnit.MILES);

        geo.search(radius)
                .flatMapIterable(Function.identity())
                .doOnNext(System.out::println)
                .subscribe();
    }
}
