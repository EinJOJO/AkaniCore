package it.einjojo.akani.core.velocity.player;

import com.velocitypowered.api.proxy.ProxyServer;
import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.player.AkaniPlayerFactory;
import it.einjojo.akani.core.player.CommonAkaniOfflinePlayer;

import java.util.UUID;

public class VelocityPlayerFactory implements AkaniPlayerFactory {
    private final InternalAkaniCore core;
    private final ProxyServer proxyServer;

    public VelocityPlayerFactory(InternalAkaniCore core, ProxyServer proxyServer) {
        this.core = core;
        this.proxyServer = proxyServer;
    }

    @Override
    public AkaniOfflinePlayer offlinePlayer(UUID playerUuid, String playerName) {
        return new CommonAkaniOfflinePlayer(core, playerUuid, playerName);
    }

    @Override
    public AkaniPlayer player(UUID playerUuid, String playerName, String serverName) {
        return new VelocityAkaniPlayer(core, playerUuid, playerName, serverName, proxyServer);
    }
}
