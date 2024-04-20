package it.einjojo.akani.core.player;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.ImmutableList;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.messaging.ChannelMessage;
import it.einjojo.akani.core.api.messaging.ChannelReceiver;
import it.einjojo.akani.core.api.messaging.MessageProcessor;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.api.player.AkaniPlayerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CommonPlayerManager implements AkaniPlayerManager, MessageProcessor {
    private static final String PLAYER_UPDATE_MESSAGE_TYPE_ID = "player_update";
    private static final Logger log = LoggerFactory.getLogger(CommonPlayerManager.class);
    private final CommonPlayerStorage commonPlayerStorage;
    private final BrokerService brokerService;

    private final Map<UUID, AkaniPlayer> onlinePlayers = new HashMap<>();
    private final Cache<String, UUID> nameToUUIDCache = Caffeine.newBuilder().build();

    public CommonPlayerManager(CommonPlayerStorage commonPlayerStorage, BrokerService brokerService) {
        this.commonPlayerStorage = commonPlayerStorage;
        this.brokerService = brokerService;
        brokerService.registerMessageProcessor(this);
    }

    @Override
    public Optional<AkaniPlayer> onlinePlayerByName(String name) {
        var uuid = nameToUUIDCache.getIfPresent(name);
        if (uuid != null) {
            return onlinePlayer(uuid);
        }
        uuid = commonPlayerStorage.playerUUIDByName(name);
        if (uuid != null) {
            nameToUUIDCache.put(name, uuid);
        }
        return onlinePlayer(uuid);
    }

    @Override
    public CompletableFuture<Optional<AkaniOfflinePlayer>> loadPlayerByName(String name) {
        return CompletableFuture.supplyAsync(() -> {
            var uuid = nameToUUIDCache.getIfPresent(name);
            if (uuid == null) {
                uuid = commonPlayerStorage.playerUUIDByName(name);
                if (uuid != null) {
                    nameToUUIDCache.put(name, uuid);
                }
            }
            if (uuid == null) {
                return Optional.empty();
            }
            var optionalOnline = onlinePlayer(uuid);
            if (optionalOnline.isPresent()) {
                return Optional.of(optionalOnline.get());
            }
            return Optional.ofNullable(commonPlayerStorage.loadOfflinePlayer(uuid));
        });
    }

    /**
     * Loads all online players from the redis storage into the normal storage.
     */
    public void loadOnlinePlayers() {
        for (AkaniPlayer player : commonPlayerStorage.onlinePlayers()) {
            onlinePlayers.put(player.uuid(), player);
        }
        log.info("Loaded {} online players", onlinePlayers.size());
    }

    @Override
    public List<AkaniPlayer> onlinePlayers() {
        return ImmutableList.copyOf(onlinePlayers.values());
    }

    @Override
    public Optional<AkaniPlayer> onlinePlayer(UUID uuid) {
        return Optional.ofNullable(onlinePlayers.get(uuid));
    }

    public void updateOnlinePlayer(AkaniPlayer player) {
        onlinePlayers.put(player.uuid(), player);
        commonPlayerStorage.updateOnlinePlayer(player);
        publishUpdate(player.uuid());
        log.debug("Updated player {} in online players", player.uuid());
    }

    public void removeOnlinePlayer(UUID uuid) {
        onlinePlayers.remove(uuid);
        commonPlayerStorage.removeOnlinePlayer(uuid);
        publishUpdate(uuid);
        log.debug("Removed player {} from online players", uuid);
    }

    public void publishUpdate(UUID uuid) {
        brokerService.publish(ChannelMessage.builder()
                .channel(processingChannel())
                .messageTypeID(PLAYER_UPDATE_MESSAGE_TYPE_ID)
                .content(uuid.toString())
                .recipient(ChannelReceiver.all())
                .build());
    }

    @Override
    public CompletableFuture<Optional<AkaniOfflinePlayer>> loadPlayer(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            var player = onlinePlayer(uuid);
            if (player.isPresent()) {
                return Optional.of(player.get());
            }
            return Optional.ofNullable(commonPlayerStorage.loadOfflinePlayer(uuid));
        });
    }

    @Override
    public void processMessage(ChannelMessage message) {
        if (message.messageTypeID().equals(PLAYER_UPDATE_MESSAGE_TYPE_ID)) {
            var uuid = UUID.fromString(message.content());
            var player = commonPlayerStorage.onlinePlayer(uuid);
            if (player != null) {
                onlinePlayers.put(uuid, player);
            } else {
                onlinePlayers.remove(uuid);
            }

        }
    }
}
