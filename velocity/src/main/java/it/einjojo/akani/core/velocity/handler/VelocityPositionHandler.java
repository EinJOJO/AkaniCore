package it.einjojo.akani.core.velocity.handler;

import com.google.gson.Gson;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.handler.position.AbstractPositionHandler;

import java.util.UUID;

public class VelocityPositionHandler extends AbstractPositionHandler {
    public VelocityPositionHandler(BrokerService brokerService, Gson gson) {
        super(brokerService, gson);
    }

    @Override
    public boolean isPlayerOnline(UUID player) {
        return true;
    }

    @Override
    public NetworkLocation positionLocally(UUID player) {
        throw new UnsupportedOperationException("Velocity does not support this operation.");
    }

    @Override
    public void teleportLocally(UUID player, NetworkLocation location) {
        throw new UnsupportedOperationException("Velocity does not support this operation.");
    }
}
