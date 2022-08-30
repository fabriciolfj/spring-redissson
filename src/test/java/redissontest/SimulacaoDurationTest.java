package redissontest;

import com.github.fariciolfj.redisspring.entity.Customer;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SimulacaoDurationTest {

    @Test
    void testEvents() throws InterruptedException {
        var sink = Sinks.many().unicast().onBackpressureBuffer();

        int index = 0;
        while(index < 1_000_000) {
            sink.tryEmitNext(index);
            index++;
        }

        var flux = sink.asFlux();

        flux.buffer(Duration.ofSeconds(1))
                .subscribe(c -> System.out.println(c));

        //Thread.sleep(1000);
    }

    @Test
    void testGrouping() {
        var customers = Arrays.asList(
                new Customer("teste1", "bom"),
                new Customer("teste2", "bom"),
                new Customer("teste3", "ruim"),
                new Customer("teste4", "ruim"));

        var result = customers.stream().collect(Collectors.groupingBy(c -> c.getStatus()));

        System.out.println(result.entrySet());
    }
}
