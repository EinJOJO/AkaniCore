package it.einjojo.akani.core.handler;

import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.api.player.AkaniPlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PositionHandler {

    CompletableFuture<NetworkLocation> position(UUID player, String serverName);

    void teleport(AkaniPlayer player, NetworkLocation location);
}
