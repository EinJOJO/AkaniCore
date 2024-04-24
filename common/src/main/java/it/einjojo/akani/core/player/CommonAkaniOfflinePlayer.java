package it.einjojo.akani.core.player;

import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.economy.EconomyHolder;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonAkaniOfflinePlayer implements AkaniOfflinePlayer {

    protected final InternalAkaniCore core;
    private final UUID uuid;
    private final String name;


    public CommonAkaniOfflinePlayer(InternalAkaniCore core, UUID uuid, String name) {
        this.core = core;
        this.uuid = uuid;
        this.name = name;
    }

    public InternalAkaniCore core() {
        return core;
    }

    @Override
    public UUID uuid() {
        return uuid;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "CommonAkaniOfflinePlayer{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public PlaytimeHolder playtime() {
        return core.playtimeManager().playtimeHolder(uuid());
    }

    @Override
    public EconomyHolder coins() {
        return core.coinsManager().playerEconomy(uuid()).orElseThrow();
    }

    @Override
    public CompletableFuture<PlaytimeHolder> playtimeAsync() {
        return core.playtimeManager().playtimeHolderAsync(uuid());
    }

    @Override
    public CompletableFuture<EconomyHolder> coinsAsync() {
        return core.coinsManager().playerEconomyAsync(uuid()).thenApply(Optional::orElseThrow);
    }

    @Override
    public CompletableFuture<EconomyHolder> thalerAsync() {
        return core.thalerManager().playerEconomyAsync(uuid()).thenApply(Optional::orElseThrow);
    }

    @Override
    public EconomyHolder thaler() {
        return core.thalerManager().playerEconomy(uuid()).orElseThrow();
    }


}
