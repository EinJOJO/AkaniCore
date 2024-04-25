package it.einjojo.akani.core.api.service;

import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.api.player.AkaniPlayer;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface BackService {
    CompletableFuture<Optional<NetworkLocation>> loadBackLocation(UUID uuid);

    void saveBackLocation(UUID uuid, NetworkLocation location);

    void deleteBackLocation(UUID uuid);

    /**
     * @return true if the player was teleported back, false if there was no back location
     */
    CompletableFuture<Boolean> teleportBackAsync(AkaniPlayer player);
}
