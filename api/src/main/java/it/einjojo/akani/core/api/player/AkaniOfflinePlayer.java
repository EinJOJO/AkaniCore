package it.einjojo.akani.core.api.player;

import it.einjojo.akani.core.api.economy.EconomyHolder;
import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface AkaniOfflinePlayer {
    UUID uuid();
    String name();
    boolean isOnline();
    PlaytimeHolder playtime();
    CompletableFuture<PlaytimeHolder> playtimeAsync();

    EconomyHolder coins();
    CompletableFuture<EconomyHolder> coinsAsync();

    EconomyHolder thaler();
    CompletableFuture<EconomyHolder> thalerAsync();

}
