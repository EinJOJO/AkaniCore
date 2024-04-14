package it.einjojo.akani.core.api;

import it.einjojo.akani.core.api.database.DatabaseProvider;
import it.einjojo.akani.core.api.economy.EconomyManager;
import it.einjojo.akani.core.api.message.MessageManager;
import it.einjojo.akani.core.api.messaging.BrokerService;
import redis.clients.jedis.JedisPool;

import java.util.logging.Logger;

/**
 * Main interface for AkaniCore
 */
public interface AkaniCore {
    JedisPool jedisPool();
    DatabaseProvider databaseProvider();
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

    Logger logger();


}
