package it.einjojo.akani.core.player;

import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.economy.EconomyHolder;
import it.einjojo.akani.core.api.network.Server;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;

import java.util.UUID;

public record CommonAkaniPlayerImpl(InternalAkaniCore core, UUID uuid, String name, String serverName) implements AkaniPlayer {
    @Override
    public PlaytimeHolder playtime() {
        return core.playtimeManager().playtimeHolder(uuid());
    }

    @Override
    public EconomyHolder coins() {
        return core.coinsManager()(uuid());
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
