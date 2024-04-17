package it.einjojo.akani.core;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.api.economy.EconomyManager;
import it.einjojo.akani.core.api.message.MessageManager;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.network.NetworkManager;
import it.einjojo.akani.core.api.network.Server;
import it.einjojo.akani.core.api.player.AkaniPlayerManager;
import it.einjojo.akani.core.api.player.playtime.PlaytimeManager;
import it.einjojo.akani.core.api.util.SimpleCloudnetAPI;
import it.einjojo.akani.core.config.MariaDbConfig;
import it.einjojo.akani.core.config.RedisCredentials;
import it.einjojo.akani.core.economy.CoinsEconomyManager;
import it.einjojo.akani.core.economy.EconomyStorage;
import it.einjojo.akani.core.economy.ThalerEconomyManager;
import it.einjojo.akani.core.handler.CloudnetConnectionHandler;
import it.einjojo.akani.core.handler.ConnectionHandler;
import it.einjojo.akani.core.handler.DummyConnectionHandler;
import it.einjojo.akani.core.messaging.RedisBrokerService;
import it.einjojo.akani.core.network.CommonNetworkManager;
import it.einjojo.akani.core.network.CommonServer;
import it.einjojo.akani.core.player.CommonPlayerManager;
import it.einjojo.akani.core.player.PlayerStorage;
import it.einjojo.akani.core.player.playtime.CommonPlaytimeManager;
import it.einjojo.akani.core.player.playtime.PlaytimeStorage;
import it.einjojo.akani.core.util.HikariFactory;
import it.einjojo.akani.core.util.JedisPoolFactory;
import redis.clients.jedis.JedisPool;

import java.util.UUID;
import java.util.logging.Logger;

public abstract class AbstractAkaniCore implements InternalAkaniCore {
    private final Server me;
    private final SimpleCloudnetAPI cloudnetAPI;
    private final Logger logger;
    private final JedisPool jedisPool;
    private final HikariDataSource dataSource;
    private final BrokerService brokerService;
    private final Gson gson;

    //server
    private final NetworkManager networkManager;
    //economy
    private final EconomyStorage coinsStorage;
    private final EconomyManager coinsEconomyManager;
    private final EconomyStorage thalerStorage;
    private final EconomyManager thalerEconomyManager;
    //player
    private final PlaytimeStorage playtimeStorage;
    private final PlaytimeManager playtimeManager;
    private final PlayerStorage playerStorage;
    private final AkaniPlayerManager playerManager;
    private final ConnectionHandler connectionHandler;

    /**
     * Called on the plugin's onEnable
     *
     * @param pluginLogger     the Logger of the plugin
     * @param redisCredentials the credentials for the redis server
     * @param mariaDBConfig    the configuration for the MariaDB database
     */
    protected AbstractAkaniCore(Logger pluginLogger, RedisCredentials redisCredentials, MariaDbConfig mariaDBConfig) {
        logger = pluginLogger;
        if (SimpleCloudnetAPI.isAvailable()) {
            cloudnetAPI = new SimpleCloudnetAPI();
            me = new CommonServer(this, cloudnetAPI.getServiceName(), cloudnetAPI.getServiceTaskName());
            connectionHandler = new CloudnetConnectionHandler(cloudnetAPI);
        } else {
            cloudnetAPI = null;
            me = new CommonServer(this, UUID.randomUUID().toString(), "local");
            connectionHandler = new DummyConnectionHandler();
        }
        dataSource = HikariFactory.create(mariaDBConfig);
        jedisPool = JedisPoolFactory.create(redisCredentials);
        gson = new Gson();
        brokerService = new RedisBrokerService(me.name(), me.groupName(), pluginLogger, jedisPool, gson);
        //server
        networkManager = new CommonNetworkManager(brokerService, this);

        //economy
        coinsStorage = new EconomyStorage("core_economy_coins", dataSource);
        thalerStorage = new EconomyStorage("core_economy_thaler", dataSource);
        coinsEconomyManager = new CoinsEconomyManager(brokerService, coinsStorage);
        thalerEconomyManager = new ThalerEconomyManager(this.brokerService, thalerStorage);
        // ...
        playtimeStorage = new PlaytimeStorage("core_playtime", jedisPool, dataSource);
        playtimeManager = new CommonPlaytimeManager(playtimeStorage);
        playerStorage = new PlayerStorage("core_players", jedisPool, dataSource, this);
        playerManager = new CommonPlayerManager(playerStorage, brokerService);

    }

    @Override
    public AkaniPlayerManager playerManager() {
        return playerManager;
    }


    public PlayerStorage playerStorage() {
        return playerStorage;
    }

    public void load() {
        logger.info("Loading Akani Core...");
        networkManager.register(me);
        brokerService().connect();
        coinsStorage.seedTables();
        thalerStorage.seedTables();
        playtimeStorage.seedTables();
        playerManager().loadOnlinePlayers();
    }

    public void unload() {
        logger.info("Unloading Akani Core...");
        networkManager.unregister(me);
        brokerService().disconnect();
        dataSource.close();
        jedisPool.close();
    }


    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public HikariDataSource dataSource() {
        Preconditions.checkNotNull(dataSource, "Data source is not initialized");
        return dataSource;
    }

    @Override
    public BrokerService brokerService() {
        Preconditions.checkNotNull(brokerService, "Broker service is not initialized");
        return brokerService;
    }

    @Override
    public EconomyManager coinsManager() {
        Preconditions.checkNotNull(coinsEconomyManager, "Coins economy manager is not initialized");
        return coinsEconomyManager;
    }

    @Override
    public NetworkManager networkManager() {
        Preconditions.checkNotNull(networkManager, "Network manager is not initialized");
        return networkManager;
    }

    @Override
    public EconomyManager thalerManager() {
        Preconditions.checkNotNull(thalerEconomyManager, "Thaler economy manager is not initialized");
        return thalerEconomyManager;
    }

    @Override
    public MessageManager messageManager() {
        return null;
    }

    @Override
    public ConnectionHandler connectionHandler() {
        return connectionHandler;
    }

    @Override
    public PlaytimeManager playtimeManager() {
        Preconditions.checkNotNull(playtimeManager, "Playtime manager is not initialized");
        return playtimeManager;
    }

    @Override
    public JedisPool jedisPool() {
        Preconditions.checkNotNull(jedisPool, "Jedis pool is not initialized");
        return jedisPool;
    }

    @Override
    public Gson gson() {
        Preconditions.checkNotNull(gson, "Gson is not initialized");
        return gson;
    }


    public Server me() {
        return me;
    }

    public SimpleCloudnetAPI cloudnetAPI() {
        return cloudnetAPI;
    }

    public EconomyStorage coinsStorage() {
        return coinsStorage;
    }

    public EconomyManager coinsEconomyManager() {
        return coinsEconomyManager;
    }

    public EconomyStorage thalerStorage() {
        return thalerStorage;
    }

    public EconomyManager thalerEconomyManager() {
        return thalerEconomyManager;
    }

    public PlaytimeStorage playtimeStorage() {
        return playtimeStorage;
    }
}
