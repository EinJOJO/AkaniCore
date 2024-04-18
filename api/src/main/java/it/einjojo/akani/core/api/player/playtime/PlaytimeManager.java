package it.einjojo.akani.core.api.player.playtime;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlaytimeManager {
    /**
     * Get the playtime holder for the given UUID
     *
     * @param uuid the uuid of the player
     * @return the playtime holder and a Zero-PlaytimeHolder if the player has no playtime
     */
    PlaytimeHolder playtimeHolder(UUID uuid);

    /**
     * Get the playtime holder for the given UUID async
     *
     * @param uuid the uuid of the player
     * @return the playtime holder and a Zero-PlaytimeHolder if the player has no playtime
     */
    CompletableFuture<PlaytimeHolder> playtimeHolderAsync(UUID uuid);

    /**
     * Update the playtime holder in the database and publishes changes to other servers.
     *
     * @param playtimeHolder the playtime holder to update
     */
    void updatePlaytime(PlaytimeHolder playtimeHolder);



    void createPlaytime(UUID uuid);
}
