package it.einjojo.akani.core;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
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
import it.einjojo.akani.core.api.util.SimpleCloudnetAPI;
import it.einjojo.akani.core.config.MariaDbConfig;
import it.einjojo.akani.core.config.RedisCredentials;
import it.einjojo.akani.core.economy.CoinsEconomyManager;
import it.einjojo.akani.core.economy.CommonEconomyStorage;
import it.einjojo.akani.core.economy.ThalerEconomyManager;
import it.einjojo.akani.core.handler.connection.CloudnetConnectionHandler;
import it.einjojo.akani.core.handler.connection.ConnectionHandler;
import it.einjojo.akani.core.handler.connection.DummyConnectionHandler;
import it.einjojo.akani.core.handler.permission.LuckPermsPermissionCheckHandler;
import it.einjojo.akani.core.handler.permission.PermissionCheckHandler;
import it.einjojo.akani.core.home.CommonHomeManager;
import it.einjojo.akani.core.home.CommonHomeStorage;
import it.einjojo.akani.core.message.CommonMessageProvider;
import it.einjojo.akani.core.message.CommonMessageStorage;
import it.einjojo.akani.core.messaging.RedisBrokerService;
import it.einjojo.akani.core.network.CommonNetworkManager;
import it.einjojo.akani.core.network.CommonServer;
import it.einjojo.akani.core.player.CommonPlayerManager;
import it.einjojo.akani.core.player.CommonPlayerStorage;
import it.einjojo.akani.core.player.playtime.CommonPlaytimeManager;
import it.einjojo.akani.core.player.playtime.CommonPlaytimeStorage;
import it.einjojo.akani.core.service.CommonBackService;
import it.einjojo.akani.core.tags.CommonTagFactory;
import it.einjojo.akani.core.tags.CommonTagManager;
import it.einjojo.akani.core.tags.CommonTagsStorage;
import it.einjojo.akani.core.util.HikariDataSourceProxyImpl;
import it.einjojo.akani.core.util.HikariFactory;
import it.einjojo.akani.core.util.JedisPoolFactory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPermsProvider;
import redis.clients.jedis.JedisPool;

import java.security.Permission;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class AbstractAkaniCore implements InternalAkaniCore {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Server me;
    private final SimpleCloudnetAPI cloudnetAPI;
    private final Logger logger;
    private final JedisPool jedisPool;
    private final HikariDataSource dataSource;
    private final HikariDataSourceProxy dataSourceProxy;
    private final BrokerService brokerService;
    private final Gson gson;
    //message
    private final CommonMessageStorage messageStorage;
    private final Set<MessageProvider> messageProviders;
    //server
    private final NetworkManager networkManager;
    //economy
    private final CommonEconomyStorage coinsStorage;
    private final EconomyManager coinsEconomyManager;
    private final CommonEconomyStorage thalerStorage;
    private final EconomyManager thalerEconomyManager;
    //player
    private final CommonPlaytimeStorage commonPlaytimeStorage;
    private final PlaytimeManager playtimeManager;
    private final CommonPlayerStorage commonPlayerStorage;
    private final AkaniPlayerManager playerManager;
    private final ConnectionHandler connectionHandler;
    private final BackService backService;
    private final CommonHomeStorage homeStorage;
    private final HomeManager homeManager;
    private final PermissionCheckHandler permissionCheckHandler;
    private final TagManager tagManager;
    boolean shuttingDown = false;

    /**
     * Called on the plugin's onEnable
     *
     * @param pluginLogger     the Logger of the plugin
     * @param redisCredentials the credentials for the redis server
     * @param mariaDBConfig    the configuration for the MariaDB database
     */
    protected AbstractAkaniCore(Logger pluginLogger, RedisCredentials redisCredentials, MariaDbConfig mariaDBConfig) {
        logger = pluginLogger;
        messageProviders = new HashSet<>();
        if (SimpleCloudnetAPI.isAvailable()) {
            cloudnetAPI = new SimpleCloudnetAPI();
            me = new CommonServer(this, cloudnetAPI.getServiceName(), cloudnetAPI.getServiceTaskName());
            connectionHandler = new CloudnetConnectionHandler(cloudnetAPI);
        } else {
            cloudnetAPI = null;
            me = new CommonServer(this, UUID.randomUUID().toString(), "local");
            connectionHandler = new DummyConnectionHandler();
        }
        dataSource =  new HikariFactory().create(mariaDBConfig);
        dataSourceProxy = new HikariDataSourceProxyImpl(dataSource);
        jedisPool = JedisPoolFactory.create(redisCredentials);
        gson = new Gson();
        brokerService = new RedisBrokerService(me.name(), me.groupName(), pluginLogger, jedisPool, gson);
        //server
        networkManager = new CommonNetworkManager(brokerService, this);

        //economy
        coinsStorage = new CommonEconomyStorage("core_economy_coins", dataSource);
        thalerStorage = new CommonEconomyStorage("core_economy_thaler", dataSource);
        coinsEconomyManager = new CoinsEconomyManager(brokerService, coinsStorage);
        thalerEconomyManager = new ThalerEconomyManager(this.brokerService, thalerStorage);
        // ...
        commonPlaytimeStorage = new CommonPlaytimeStorage("core_playtime", jedisPool, dataSource);
        playtimeManager = new CommonPlaytimeManager(commonPlaytimeStorage);
        commonPlayerStorage = new CommonPlayerStorage("core_players", jedisPool, dataSource, this);
        playerManager = new CommonPlayerManager(commonPlayerStorage, brokerService);
        messageStorage = new CommonMessageStorage(dataSource);
        backService = new CommonBackService(this);
        permissionCheckHandler = createPermissionCheckHandler();

        tagManager = new CommonTagManager(new CommonTagsStorage(permissionCheckHandler, dataSourceProxy,
                new CommonTagFactory(miniMessage()), "core_" ));

        // home
        homeManager = new CommonHomeManager(homeStorage = new CommonHomeStorage("core_", dataSource, gson,
                createHomeFactory()));
    }



    public PermissionCheckHandler createPermissionCheckHandler() {
        return new LuckPermsPermissionCheckHandler(LuckPermsProvider.get());
    }

    @Override
    public MiniMessage miniMessage() {
        return miniMessage;
    }

    @Override
    public HikariDataSourceProxy dataSourceProxy() {
        return dataSourceProxy;
    }

    @Override
    public BackService backService() {
        return backService;
    }

    @Override
    public boolean shuttingDown() {
        return shuttingDown;
    }

    @Override
    public TagManager tagManager() {
        return tagManager;
    }

    @Override
    public AkaniPlayerManager playerManager() {
        return playerManager;
    }


    public CommonPlayerStorage playerStorage() {
        return commonPlayerStorage;
    }

    public CommonMessageStorage messageStorage() {
        return messageStorage;
    }

    @Override
    public void registerMessageProvider(MessageProvider messageProvider) {
        messageProviders.add(messageProvider);
        delayedMessageReload();
    }

    public void loadProviders() {
        for (MessageProvider provider : messageProviders) {
            if (provider.shouldInsert(messageStorage())) {
                provider.insertMessages(messageStorage());
            }
        }
    }


    public abstract void delayedMessageReload();

    public void load() {
        logger.info("Loading Akani Core...");
        brokerService().connect();
        coinsStorage.seedTables();
        thalerStorage.seedTables();
        commonPlaytimeStorage.seedTables();
        commonPlayerStorage.seedTables();
        messageStorage.seedTables();
        homeStorage.seedTables();
        networkManager.register(me);
        registerMessageProvider(new CommonMessageProvider());
        playerManager().loadOnlinePlayers();
    }

    public void unload() {
        logger.info("Unloading Akani Core...");
        networkManager.unregister(me);
        dataSource.close();
        brokerService().disconnect();
        jedisPool.close();
    }

    @Override
    public Server server() {
        return me;
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
    public PermissionCheckHandler permissionCheckHandler() {
        return permissionCheckHandler;
    }

    public Set<MessageProvider> messageProviders() {
        return messageProviders;
    }

    @Override
    public MessageManager<?> messageManager(Language language) {
        if (language == Language.GERMAN) {
            return germanMessageManager();
        } else {
            return englishMessageManager();
        }
    }

    public abstract MessageManager<?> germanMessageManager();

    public abstract MessageManager<?> englishMessageManager();


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

    public CommonEconomyStorage coinsStorage() {
        return coinsStorage;
    }

    public EconomyManager coinsEconomyManager() {
        return coinsEconomyManager;
    }

    public CommonEconomyStorage thalerStorage() {
        return thalerStorage;
    }

    public EconomyManager thalerEconomyManager() {
        return thalerEconomyManager;
    }

    public CommonPlaytimeStorage playtimeStorage() {
        return commonPlaytimeStorage;
    }

    @Override
    public HomeManager homeManager() {
        return homeManager;
    }
}
