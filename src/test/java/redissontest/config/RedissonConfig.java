package redissontest.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;

import java.util.Objects;

public class RedissonConfig {

    private RedissonClient redissonClient;

    public RedissonClient getClient() {
        if (Objects.isNull(redissonClient)) {
            final Config config = new Config();
            config.useSingleServer()
                    .setAddress("redis://127.0.0.1:6379")
                    .setUsername("sam")
                    .setPassword("pass123");

            redissonClient = Redisson.create(config);
        }

        return redissonClient;
    }

    public RedissonReactiveClient getReactiveClient() {
        return getClient().reactive();
    }
}
