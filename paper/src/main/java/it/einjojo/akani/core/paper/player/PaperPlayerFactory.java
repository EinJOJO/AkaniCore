package it.einjojo.akani.core.paper.player;

import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.player.AkaniPlayerFactory;
import it.einjojo.akani.core.player.CommonAkaniOfflinePlayer;

import java.util.UUID;

public class PaperPlayerFactory implements AkaniPlayerFactory {

    private final InternalAkaniCore core;

    public PaperPlayerFactory(InternalAkaniCore core) {
        this.core = core;
    }

    @Override
    public AkaniOfflinePlayer offlinePlayer(UUID playerUuid, String playerName) {
        return new CommonAkaniOfflinePlayer(core, playerUuid, playerName);
    }

    @Override
    public AkaniPlayer player(UUID playerUuid, String playerName, String serverName) {
        return new PaperAkaniPlayer(core, playerUuid, playerName, serverName);
    }
}
