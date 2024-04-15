package it.einjojo.akani.core;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.api.economy.EconomyManager;
import it.einjojo.akani.core.api.message.MessageManager;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.network.Server;
import it.einjojo.akani.core.config.MariaDBCredentials;
import it.einjojo.akani.core.config.RedisCredentials;
import it.einjojo.akani.core.messaging.RedisBrokerService;
import it.einjojo.akani.core.util.HikariFactory;
import it.einjojo.akani.core.util.JedisPoolFactory;
import redis.clients.jedis.JedisPool;

import java.util.logging.Logger;

public abstract class AbstractAkaniCore implements InternalAkaniCore {
    private final Logger logger;
    private final JedisPool jedisPool;
    private final HikariDataSource dataSource;
    private final BrokerService brokerService;
    private final Gson gson;

    protected AbstractAkaniCore(Server executor, Logger pluginLogger, RedisCredentials redisCredentials, MariaDBCredentials mariaDBCredentials) {
        this.logger = pluginLogger;
        dataSource = HikariFactory.create(mariaDBCredentials);
        this.jedisPool = JedisPoolFactory.create(redisCredentials);
        this.gson = new Gson();
        brokerService = new RedisBrokerService(executor.name(), executor.group(), pluginLogger, jedisPool, gson);
    }

    public void load() {
        brokerService().connect();
    }

    public void unload() {
        brokerService().disconnect();
    }


    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public HikariDataSource dataSource() {
        return dataSource;
    }

    @Override
    public BrokerService brokerService() {
        return brokerService;
    }

    @Override
    public EconomyManager coinsManager() {
        return null;
    }

    @Override
    public EconomyManager thalerManager() {
        return null;
    }

    @Override
    public MessageManager messageManager() {
        return null;
    }

    @Override
    public JedisPool jedisPool() {
        return jedisPool;
    }

    @Override
    public Gson gson() {
        return gson;
    }


}
