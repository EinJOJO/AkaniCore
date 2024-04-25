package it.einjojo.akani.core.api.player;

import it.einjojo.akani.core.api.economy.EconomyHolder;
import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface AkaniOfflinePlayer {
    UUID uuid();

    String name();

    boolean isOnline();

    PlaytimeHolder playtime();

    CompletableFuture<PlaytimeHolder> playtimeAsync();

    EconomyHolder coins();

    CompletableFuture<Boolean> hasPermission(String permission);

    CompletableFuture<String> plainPrefix();

    CompletableFuture<Component> prefix();

    CompletableFuture<String> plainSuffix();

    CompletableFuture<Component> suffix();


    CompletableFuture<EconomyHolder> coinsAsync();

    EconomyHolder thaler();

    CompletableFuture<EconomyHolder> thalerAsync();


}
