package it.einjojo.akani.core.player;

import com.google.common.collect.ImmutableList;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.messaging.ChannelMessage;
import it.einjojo.akani.core.api.messaging.ChannelReceiver;
import it.einjojo.akani.core.api.messaging.MessageProcessor;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.api.player.AkaniPlayerManager;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CommonPlayerManager implements AkaniPlayerManager, MessageProcessor {
    private static final String PLAYER_UPDATE_MESSAGE_TYPE_ID = "player_update";
    private final PlayerStorage playerStorage;
    private final BrokerService brokerService;

    private final Map<UUID, AkaniPlayer> onlinePlayers = new HashMap<>();

    public CommonPlayerManager(PlayerStorage playerStorage, BrokerService brokerService) {
        this.playerStorage = playerStorage;
        this.brokerService = brokerService;
        brokerService.registerMessageProcessor(this);
    }

    /**
     * Loads all online players from the redis storage into the normal storage.
     */
    public void loadOnlinePlayers() {
        for (AkaniPlayer player : playerStorage.onlinePlayers()) {
            onlinePlayers.put(player.uuid(), player);
        }
    }

    @Override
    public List<AkaniPlayer> onlinePlayers() {
        return ImmutableList.copyOf(onlinePlayers.values());
    }

    @Override
    public Optional<AkaniPlayer> onlinePlayer(UUID uuid) {
        return Optional.ofNullable(onlinePlayers.get(uuid));
    }

    public void addOnlinePlayer(AkaniPlayer player) {
        onlinePlayers.put(player.uuid(), player);
        playerStorage.updateOnlinePlayer(player);
        publishUpdate(player.uuid());
    }

    public void removeOnlinePlayer(UUID uuid) {
        onlinePlayers.remove(uuid);
        playerStorage.removeOnlinePlayer(uuid);
        publishUpdate(uuid);
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
            return Optional.ofNullable(playerStorage.loadOfflinePlayer(uuid));
        });
    }

    @Override
    public void processMessage(ChannelMessage message) {
        if (message.messageTypeID().equals(PLAYER_UPDATE_MESSAGE_TYPE_ID)) {
            var uuid = UUID.fromString(message.content());
            var player = playerStorage.onlinePlayer(uuid);
            if (player != null) {
                onlinePlayers.put(uuid, player);
            } else {
                onlinePlayers.remove(uuid);
            }

        }
    }
}
