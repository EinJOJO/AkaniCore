package it.einjojo.akani.core.api.player.playtime;

import java.util.UUID;

public interface PlaytimeManager {
    PlaytimeHolder playtimeHolder(UUID uuid);

    void updatePlaytime(UUID uuid, PlaytimeHolder playtimeHolder);
}
