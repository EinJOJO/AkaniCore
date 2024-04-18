package it.einjojo.akani.core.handler;

import it.einjojo.akani.core.api.network.NetworkLocation;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PositionHandler {

    CompletableFuture<NetworkLocation> position(UUID player, String serverName);

    void teleport(UUID player, String serverName, NetworkLocation location);
}
