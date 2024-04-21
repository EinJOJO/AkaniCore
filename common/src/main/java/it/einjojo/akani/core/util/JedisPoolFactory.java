package it.einjojo.akani.core.util;

import com.google.common.base.Preconditions;
import it.einjojo.akani.core.config.RedisCredentials;
import redis.clients.jedis.*;

public class JedisPoolFactory {

    public static JedisPool create(RedisCredentials credentials) {
        Preconditions.checkNotNull(credentials, "Credentials must not be null");
        Preconditions.checkNotNull(credentials.host(), "Host must not be null");
        HostAndPort hostAndPort = new HostAndPort(credentials.host(), credentials.port());
        JedisClientConfig config = DefaultJedisClientConfig.builder()
                .user(credentials.username())
                .password(credentials.password())
                .build();
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(16);
        poolConfig.setMaxIdle(1);
        return new JedisPool(poolConfig, hostAndPort, config);
    }


}
