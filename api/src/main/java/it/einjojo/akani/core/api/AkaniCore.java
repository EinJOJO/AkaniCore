package it.einjojo.akani.core.api;

import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.api.economy.EconomyManager;
import it.einjojo.akani.core.api.message.MessageManager;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.network.NetworkManager;
import it.einjojo.akani.core.api.player.AkaniPlayerManager;
import it.einjojo.akani.core.api.player.playtime.PlaytimeManager;
import org.jetbrains.annotations.ApiStatus;
import redis.clients.jedis.JedisPool;

import java.util.logging.Logger;

/**
 * Main interface for AkaniCore
 */
public interface AkaniCore {
    /**
     * @return JedisPool instance
     */
    JedisPool jedisPool();

    /**
     * @return HikariDataSource instance
     */
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

    /**
     * @return Message Manager
     */
    MessageManager messageManager();

    /**
     * @return Playtime Manager
     */
    PlaytimeManager playtimeManager();

    /**
     * @return Player Manager
     */
    AkaniPlayerManager playerManager();




    /**
     * @return Network Manager
     */
    NetworkManager networkManager();

    @ApiStatus.Internal
    Logger logger();


}
