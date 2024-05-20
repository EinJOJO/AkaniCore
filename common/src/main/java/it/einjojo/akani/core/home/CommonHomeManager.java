package it.einjojo.akani.core.home;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.einjojo.akani.core.api.home.HomeHolder;
import it.einjojo.akani.core.api.home.HomeManager;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CommonHomeManager implements HomeManager {

    private final CommonHomeStorage homeStorage;
    private final AsyncLoadingCache<UUID, HomeHolder> homeCache;

    public CommonHomeManager(CommonHomeStorage storage) {
        homeStorage = storage;
        homeCache = Caffeine.newBuilder()
                .maximumSize(100)
                .buildAsync(this::loadHomes);
    }

    @Override
    public CompletableFuture<HomeHolder> homesAsync(UUID player) {
        return homeCache.get(player);
    }

    @Override
    public HomeHolder homes(UUID player) {
        return homeCache.synchronous().get(player);
    }


    public void invalidate(UUID player) {
        homeCache.synchronous().invalidate(player);
    }

    private CompletableFuture<HomeHolder> loadHomes(UUID player, Executor executor) {
        return CompletableFuture.supplyAsync(() -> homeStorage.loadHomes(player), executor);
    }
}
