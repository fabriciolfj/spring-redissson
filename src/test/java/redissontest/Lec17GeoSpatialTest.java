package redissontest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.GeoUnit;
import org.redisson.api.RGeoReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import com.github.fariciolfj.redisspring.geo.dto.GeoLocation;
import com.github.fariciolfj.redisspring.geo.dto.Restaurant;
import com.github.fariciolfj.redisspring.geo.util.RestaurantUtil;

import java.util.function.Function;

public class Lec17GeoSpatialTest extends BaseTest {

    private RGeoReactive<Restaurant> geo;
    private RMapReactive<String, GeoLocation> map;

    @BeforeAll
    public void setGeo() {
        this.geo = this.redisClient.getGeo("restaurants", new TypedJsonJacksonCodec(Restaurant.class));
        this.map = this.redisClient.getMap("us:textas", new TypedJsonJacksonCodec(String.class, GeoLocation.class));
    }

    @Test
    public void add() {
        var test = RestaurantUtil.getRestaurants();
        Mono<Void> mono = Flux.fromIterable(test)
                .flatMap(c -> geo.add(c.getLongitude(), c.getLatitude(), c).thenReturn(c))
                .flatMap(r -> map.fastPut(r.getZip(), GeoLocation.of(r.getLongitude(), r.getLatitude())))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        /*var radius = GeoSearchArgs.from(-96.80539, 32.78136)
                .radius(3, GeoUnit.MILES);

        geo.search(radius)
                .flatMapIterable(Function.identity())
                .doOnNext(System.out::println)
                .count()
                .subscribe();*/
    }

    @Test
    public void search() {
        var result = this.map
                .get("75224")
                .map(r -> GeoSearchArgs.from(r.getLongitude(), r.getLatitude()).radius(3, GeoUnit.MILES))
                .flatMap(g -> geo.search(g))
                .flatMapIterable(Function.identity())
                .doOnNext(System.out::println)
                 .then();

        StepVerifier.create(result)
                .verifyComplete();
    }
}
