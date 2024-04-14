package it.einjojo.akani.core.api.economy;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface EconomyManager {
    Optional<EconomyHolder> cachedPlayerEconomy(UUID uuid);
    void invalidateCachedEconomy(UUID uuid);
    CompletableFuture<EconomyHolder> loadEconomy(UUID uuid);
    void updateEconomy(EconomyHolder economyHolder);
    void updateEconomyAsync(EconomyHolder economyHolder);
}
