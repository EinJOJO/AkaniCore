package it.einjojo.akani.core.api.economy;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface EconomyManager {
    /**
     * Returns only the cached player economy, if it's not cached it will return an empty optional.
     * @param uuid the player uuid
     * @return the player economy
     */
    Optional<EconomyHolder> cachedPlayerEconomy(UUID uuid);

    /**
     * Get the player economy async
     * Tries to get the cached economy, if it's not cached it will load it from the database.
     * @param uuid the player to get the economy
     * @return the player economy
     */
    CompletableFuture<EconomyHolder> playerEconomyAsync(UUID uuid);


    void invalidateCachedEconomy(UUID uuid);
    CompletableFuture<EconomyHolder> loadEconomy(UUID uuid);
    void updateEconomy(EconomyHolder economyHolder);
    void updateEconomyAsync(EconomyHolder economyHolder);
}
