package it.einjojo.akani.core.handler;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.messaging.ChannelMessage;
import it.einjojo.akani.core.api.messaging.ChannelReceiver;
import it.einjojo.akani.core.api.messaging.MessageProcessor;
import it.einjojo.akani.core.api.network.NetworkLocation;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractPositionHandler implements MessageProcessor, PositionHandler {
    protected static final Cache<UUID, NetworkLocation> openTeleports = Caffeine.newBuilder().expireAfterWrite(Duration.ofSeconds(10)).build();
    private static final String REQUEST_POSITION_MESSAGE_ID = "reqpos";
    private static final String TELEPORT_MESSAGE_ID = "tp";
    private final BrokerService brokerService;
    private final Gson gson;

    protected AbstractPositionHandler(BrokerService brokerService, Gson gson) {
        this.brokerService = brokerService;
        this.gson = gson;
        brokerService.registerMessageProcessor(this);
    }

    public BrokerService brokerService() {
        return brokerService;
    }

    @Override
    public void processMessage(ChannelMessage message) {
        if (message.messageTypeID().equals(REQUEST_POSITION_MESSAGE_ID)) {
            var player = UUID.fromString(message.content());
            var location = positionLocally(player);
            if (location == null) {
                throw new IllegalStateException("Position not found for player " + player);
            }
            var response = ChannelMessage.responseTo(message).content(serializeNetworkLocation(location)).build();
            brokerService().publish(response);
        }
        if (message.messageTypeID().equals(TELEPORT_MESSAGE_ID)) {
            var payload = ByteStreams.newDataInput(message.content().getBytes());
            var player = UUID.fromString(payload.readUTF());
            var location = deserializeNetworkLocation(payload.readUTF());
            if (isPlayerOnline(player)) {
                teleportLocally(player, location);
            } else {
                openTeleports.put(player, location);
            }
        }
    }


    public abstract boolean isPlayerOnline(UUID player);

    public abstract NetworkLocation positionLocally(UUID player);

    public abstract void teleportLocally(UUID player, NetworkLocation location);

    @Override
    public CompletableFuture<NetworkLocation> position(UUID player, String serverName) {
        if (serverName.equals(brokerService.brokerName())) {
            return CompletableFuture.completedFuture(positionLocally(player));
        }
        var message = ChannelMessage.builder().channel(processingChannel()).messageTypeID(REQUEST_POSITION_MESSAGE_ID).content(player.toString()).recipient(ChannelReceiver.server(serverName)).build();
        return brokerService().publishRequest(message).thenApply((response) -> deserializeNetworkLocation(response.content()));
    }

    @Override
    public void teleport(UUID player, String serverName, NetworkLocation location) {
        boolean alreadyOnServer = location.type().equals(NetworkLocation.Type.SERVER) && location.referenceName().equals(brokerService.brokerName());
        boolean alreadyOnGroup = location.type().equals(NetworkLocation.Type.GROUP) && location.referenceName().equals(brokerService.groupName());
        if (alreadyOnServer || alreadyOnGroup || location.type().equals(NetworkLocation.Type.UNSPECIFIED)) {
            teleportLocally(player, location);
            return;
        }


        // SEND REDIS MESSAGE TO SERVER
        var payload = ByteStreams.newDataOutput();
        payload.writeUTF(player.toString());
        payload.writeUTF(serializeNetworkLocation(location));
        ChannelReceiver receiver = location.type().equals(NetworkLocation.Type.SERVER) ? ChannelReceiver.server(location.referenceName()) : ChannelReceiver.group(location.referenceName());
        var message = ChannelMessage.builder().channel(processingChannel()).messageTypeID(TELEPORT_MESSAGE_ID).content(payload.toByteArray()).recipient(receiver).build();
        brokerService().publish(message);
    }

    public final NetworkLocation deserializeNetworkLocation(String message) {
        return gson.fromJson(message, NetworkLocation.class);
    }

    public Gson gson() {
        return gson;
    }

    public final String serializeNetworkLocation(NetworkLocation location) {
        return gson.toJson(location);
    }


}
