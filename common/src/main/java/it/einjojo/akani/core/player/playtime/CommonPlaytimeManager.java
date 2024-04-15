package it.einjojo.akani.core.player.playtime;

import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;
import it.einjojo.akani.core.api.player.playtime.PlaytimeManager;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonPlaytimeManager implements PlaytimeManager {
    public static final PlaytimeHolder EMPTY_PLAYTIME_HOLDER = new CommonPlaytimeHolder(null, 0, null, null);
    private final PlaytimeStorage playtimeStorage;

    public CommonPlaytimeManager(PlaytimeStorage playtimeStorage) {
        this.playtimeStorage = playtimeStorage;
    }

    @Override
    public PlaytimeHolder playtimeHolder(UUID uuid) {
        var ph = playtimeStorage.playtimeHolder(uuid);
        if (ph == null) return EMPTY_PLAYTIME_HOLDER;
        return ph;
    }

    @Override
    public CompletableFuture<PlaytimeHolder> playtimeHolderAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> playtimeHolder(uuid));
    }


    public void updatePlaytime(PlaytimeHolder playtimeHolder) {
        playtimeStorage.updatePlaytimeHolder(playtimeHolder);
    }
}
