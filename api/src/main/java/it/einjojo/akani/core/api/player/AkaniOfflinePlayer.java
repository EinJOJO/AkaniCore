package it.einjojo.akani.core.api.player;

import it.einjojo.akani.core.api.economy.EconomyHolder;
import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;
import it.einjojo.akani.core.api.tags.TagHolder;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface AkaniOfflinePlayer extends TagHolder {
    UUID uuid();

    String name();

    boolean isOnline();

    PlaytimeHolder playtime();

    CompletableFuture<PlaytimeHolder> playtimeAsync();

    EconomyHolder coins();

    boolean hasPermission(String permission);

    CompletableFuture<Boolean> hasPermissionAsync(String permission);

    CompletableFuture<String> plainPrefix();

    CompletableFuture<Component> prefix();

    CompletableFuture<String> plainSuffix();

    CompletableFuture<Component> suffix();

    TagHolder tagHolder();

    CompletableFuture<EconomyHolder> coinsAsync();

    EconomyHolder thaler();

    CompletableFuture<EconomyHolder> thalerAsync();


}
