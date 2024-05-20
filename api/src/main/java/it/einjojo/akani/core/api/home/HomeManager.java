package it.einjojo.akani.core.api.home;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface HomeManager {
    HomeHolder homes(UUID owner);
    CompletableFuture<HomeHolder> homesAsync(UUID owner);
}
