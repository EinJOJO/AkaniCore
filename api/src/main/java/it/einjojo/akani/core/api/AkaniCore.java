package it.einjojo.akani.core.api;

import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.api.economy.EconomyManager;
import it.einjojo.akani.core.api.home.HomeManager;
import it.einjojo.akani.core.api.message.Language;
import it.einjojo.akani.core.api.message.MessageManager;
import it.einjojo.akani.core.api.message.MessageProvider;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.network.NetworkManager;
import it.einjojo.akani.core.api.network.Server;
import it.einjojo.akani.core.api.player.AkaniPlayerManager;
import it.einjojo.akani.core.api.player.playtime.PlaytimeManager;
import it.einjojo.akani.core.api.service.BackService;
import it.einjojo.akani.core.api.tags.TagManager;
import it.einjojo.akani.core.api.util.HikariDataSourceProxy;
import net.kyori.adventure.text.Component;
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

    /**
     * @return HikariDataSourceProxy instance
     */
    HikariDataSourceProxy dataSourceProxy();

    BrokerService brokerService();

    /**
     * @return Main Currency Manager
     */
    EconomyManager coinsManager();

    /**
     * @return Premium Currency Manager (Rubin)
     * Use {@link #rubinManager()} instead
     */
    @Deprecated
    EconomyManager thalerManager();

    /**
     * @return Premium Currency Manager (Rubin)
     */
    EconomyManager rubinManager();

    /**
     * @return default german message manager instance
     */
    default MessageManager<?> messageManager() {
        return messageManager(Language.GERMAN);
    }

    MessageManager<?> messageManager(Language language);

    /**
     * Register a new MessageProvider to the core to extend the message system
     *
     * @param messageProvider the provider to register
     */
    void registerMessageProvider(MessageProvider messageProvider);

    /**
     * @return Playtime Manager
     */
    PlaytimeManager playtimeManager();

    /**
     * @return Player Manager
     */
    AkaniPlayerManager playerManager();


    /**
     * Provides methods to interact with the back location system
     *
     * @return BackService instance
     */
    BackService backService();

    HomeManager homeManager();

    /**
     * @return Network Manager
     */
    NetworkManager networkManager();

    default String serverName() {
        return server().name();
    }

    default String groupName() {
        return server().groupName();
    }

    Server server();

    @ApiStatus.Internal
    Logger logger();

    void broadcast(Component message);

    void broadcast(String miniMessage);

    TagManager tagManager();

}
