package it.einjojo.akani.core.player;

import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.economy.EconomyHolder;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.api.network.Server;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record CommonAkaniPlayer(InternalAkaniCore core, UUID uuid, String name, String serverName) implements AkaniPlayer {
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
    public void connect(String servername) {

    }

    @Override
    public void teleport(NetworkLocation networkLocation) {

    }

    @Override
    public CompletableFuture<NetworkLocation> location() {
        return null;
    }

    @Override
    public EconomyHolder thaler() {
        return null;
    }

    @Override
    public Server server() {
        return null;
    }
}
