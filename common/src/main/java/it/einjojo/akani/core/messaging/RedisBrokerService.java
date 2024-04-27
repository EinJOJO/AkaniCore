package it.einjojo.akani.core.messaging;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import it.einjojo.akani.core.api.messaging.ChannelMessage;
import it.einjojo.akani.core.util.ServiceUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class RedisBrokerService extends AbstractBrokerService {
    private final Logger logger;
    private final Set<String> subscribedChannels = new HashSet<>();
    private final RedisBrokerPubSub pubSub;
    private final JedisPool pool;
    private final Gson gson;
    private ExecutorService pubSubService;
    private Jedis pubSubJedis;
    private boolean connected;

    public RedisBrokerService(String name, String group, Logger logger, JedisPool pool, Gson gson) {
        super(name, group, logger);
        this.logger = logger;
        this.gson = gson;
        pubSub = new RedisBrokerPubSub(this, gson);
        this.pool = pool;
        logger.info("Redis broker service initialized with name: " + name + " and group: " + group);
    }


    @Override
    public void connect() {
        if (connected) return;
        if (subscribedChannels.isEmpty()) {
            logger.info("Currently no channels to subscribe to. Skipping establishing connection.");
            return;
        }
        pubSubJedis = pool.getResource();
        this.pubSubService = Executors.newSingleThreadExecutor();
        logger.info("Connecting to redis pub sub service...");
        pubSubService.execute(() -> {
            pubSubJedis.subscribe(pubSub, subscribedChannels.toArray(new String[0]));
        });
        connected = true;
    }

    @Override
    public void disconnect() {
        if (!connected) return;
        Preconditions.checkArgument(pubSubJedis != null, "Jedis not initialized");
        logger.info("Disconnecting from redis pub sub service...");
        pubSub.unsubscribe();
        pubSubJedis.close(); // return to pool
        pubSubJedis = null;
        ServiceUtil.close(pubSubService);
        connected = false;
        logger.info("Disconnected from redis pub sub service");
    }

    @Override
    public void publish(ChannelMessage message) {
        try (Jedis jedis = pool.getResource()) {
            String payload = gson.toJson(message);
            jedis.publish(message.channel(), payload);
        }
    }

    @Override
    public void subscribe(String channel) {
        if (!subscribedChannels.add(channel)) return; // Already subscribed
        if (connected) {
            pubSub.subscribe(channel);
            logger.info("Subscribed to channel: " + channel + "(" + pubSub.getSubscribedChannels() + ") on redis pub sub service");
        }

    }

    @Override
    public void unsubscribe(String channel) {
        subscribedChannels.remove(channel);
        if (!connected) return; // No need to unsubscribe if not connected
        pubSub.unsubscribe(channel);
        logger.info("Unsubscribed from channel: " + channel + "(" + pubSub.getSubscribedChannels() + ") on redis pub sub service");
        if (subscribedChannels.isEmpty()) {
            logger.info("Unsubscribed from all channels. ");
            disconnect();
        }
    }

    @Override
    public Logger logger() {
        return logger;
    }

    public Set<String> subscribedChannels() {
        return subscribedChannels;
    }

    public RedisBrokerPubSub pubSub() {
        return pubSub;
    }

    public JedisPool pool() {
        return pool;
    }

    public Gson gson() {
        return gson;
    }


    public boolean isConnected() {
        return connected;
    }


}