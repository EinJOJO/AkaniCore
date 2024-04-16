package it.einjojo.akani.core.handler;

import com.google.gson.Gson;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.messaging.ChannelMessage;
import it.einjojo.akani.core.api.messaging.ChannelReceiver;
import it.einjojo.akani.core.api.messaging.MessageProcessor;
import it.einjojo.akani.core.api.network.NetworkLocation;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractPositionHandler implements MessageProcessor {
    private static final String REQUEST_POSITION_MESSAGE_ID = "reqpos";
    private static final String TELEPORT_MESSAGE_ID = "tp";
    private final BrokerService brokerService;
    private final Gson gson;

    protected AbstractPositionHandler(BrokerService brokerService, Gson gson) {
        this.brokerService = brokerService;
        this.gson = gson;
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
    }

    public abstract NetworkLocation positionLocally(UUID player);

    public abstract void teleportLocally(UUID player, NetworkLocation location);

    public CompletableFuture<NetworkLocation> position(UUID player, String serverName) {
        if (serverName.equals(brokerService.brokerName())) {
            return CompletableFuture.completedFuture(positionLocally(player));
        }
        var message = ChannelMessage.builder().channel(processingChannel()).messageTypeID(REQUEST_POSITION_MESSAGE_ID).content(player.toString()).recipient(ChannelReceiver.server(serverName)).build();
        return brokerService().publishRequest(message).thenApply((response) -> deserializeNetworkLocation(response.content()));
    }

    public void teleport(UUID player, String serverName, NetworkLocation location) {
        if (serverName.equals(brokerService.brokerName())) {
            teleportLocally(player, location);
            return;
        }
        var message = ChannelMessage.builder().channel(processingChannel()).messageTypeID(TELEPORT_MESSAGE_ID).content(serializeNetworkLocation(location)).recipient(ChannelReceiver.server(serverName)).build();
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
