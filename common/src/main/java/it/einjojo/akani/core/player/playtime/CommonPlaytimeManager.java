package it.einjojo.akani.core.player.playtime;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;
import it.einjojo.akani.core.api.player.playtime.PlaytimeManager;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonPlaytimeManager implements PlaytimeManager {
    public static final PlaytimeHolder EMPTY_PLAYTIME_HOLDER = new CommonPlaytimeHolder(null, 0, null, null);
    private final LoadingCache<UUID, PlaytimeHolder> playtimeCache;
    private final CommonPlaytimeStorage commonPlaytimeStorage;


    public CommonPlaytimeManager(CommonPlaytimeStorage commonPlaytimeStorage) {
        this.commonPlaytimeStorage = commonPlaytimeStorage;
        playtimeCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(2))
                .build(commonPlaytimeStorage::playtimeHolder);

    }

    @Override
    public PlaytimeHolder playtimeHolder(UUID uuid) {
        var ph = playtimeCache.get(uuid);
        if (ph == null) return EMPTY_PLAYTIME_HOLDER;
        return ph;
    }

    @Override
    public CompletableFuture<PlaytimeHolder> playtimeHolderAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> playtimeHolder(uuid));
    }


    @Override
    public void createPlaytime(UUID uuid) {
        commonPlaytimeStorage.createPlaytimeHolder(new CommonPlaytimeHolder(uuid, 0, Instant.now(), Instant.now()));
    }

    public void updatePlaytime(PlaytimeHolder playtimeHolder) {
        if (playtimeHolder.ownerUuid() == null) return;
        playtimeCache.invalidate(playtimeHolder.ownerUuid());
        commonPlaytimeStorage.updatePlaytimeHolder(playtimeHolder);
    }
}
