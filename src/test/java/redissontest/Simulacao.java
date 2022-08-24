package redissontest;

import com.github.fariciolfj.redisspring.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Simulacao {

    public static void main(String[] args) {
        var just = Flux.just(new Customer("a"), new Customer("b"));
        var test = Flux.just(new Customer("a"));

        test.flatMap(c -> just.doOnNext(ec -> ec.setName("teste")))
                .subscribe(e -> System.out.println(e),
                        e -> e.printStackTrace(),
                        () -> System.out.println("complete"));
    }
}
