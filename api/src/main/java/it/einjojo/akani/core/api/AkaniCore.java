package it.einjojo.akani.core.api;

import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.api.economy.EconomyManager;
import it.einjojo.akani.core.api.message.MessageManager;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.player.playtime.PlaytimeManager;
import redis.clients.jedis.JedisPool;

import java.util.logging.Logger;

/**
 * Main interface for AkaniCore
 */
public interface AkaniCore {
    JedisPool jedisPool();
    HikariDataSource dataSource();
    BrokerService brokerService();
    /**
     * @return Main Currency Manager
     */
    EconomyManager coinsManager();

    /**
     * @return Premium Currency Manager (Thaler)
     */
    EconomyManager thalerManager();

    MessageManager messageManager();

    PlaytimeManager playtimeManager();

    Logger logger();


}
