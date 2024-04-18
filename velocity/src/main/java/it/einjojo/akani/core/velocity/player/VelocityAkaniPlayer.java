package it.einjojo.akani.core.velocity.player;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.player.CommonAkaniPlayer;
import it.einjojo.akani.core.player.CommonPlayerManager;

import java.util.Optional;
import java.util.UUID;

public class VelocityAkaniPlayer extends CommonAkaniPlayer {
    private final ProxyServer proxyServer;

    public VelocityAkaniPlayer(InternalAkaniCore core, UUID uuid, String name, String serverName, ProxyServer proxyServer) {
        super(core, uuid, name, serverName);
        this.proxyServer = proxyServer;
    }

    Optional<Player> velocityPlayer() {
        return proxyServer.getPlayer(uuid());
    }

}
