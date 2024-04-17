package it.einjojo.akani.core.api.player;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface AkaniPlayerManager {

    List<AkaniPlayer> onlinePlayers();

    Optional<AkaniPlayer> onlinePlayer(UUID uuid);

    /**
     * Load a player from the database by UUID or if the player is online from the onlineplayer-cache
     *
     * @param uuid UUID of the player
     * @return CompletableFuture with the player
     */
    CompletableFuture<Optional<AkaniOfflinePlayer>> loadPlayer(UUID uuid);

    @ApiStatus.Internal
    void loadOnlinePlayers();
}
