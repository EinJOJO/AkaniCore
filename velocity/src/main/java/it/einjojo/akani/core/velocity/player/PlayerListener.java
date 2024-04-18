package it.einjojo.akani.core.velocity.player;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import it.einjojo.akani.core.player.CommonPlayerManager;
import it.einjojo.akani.core.velocity.VelocityAkaniCorePlugin;

public class PlayerListener {
    private final VelocityAkaniCorePlugin corePlugin;

    public PlayerListener(VelocityAkaniCorePlugin corePlugin) {
        this.corePlugin = corePlugin;
        corePlugin.proxyServer().getEventManager().register(corePlugin, this);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Subscribe
    public void playerConnectHandler(ServerPostConnectEvent event) {
        var player = event.getPlayer();
        player.getCurrentServer().ifPresent((server) -> {
            var serverName = server.getServerInfo().getName();
            var akaniPlayer = corePlugin.core().playerFactory().player(player.getUniqueId(), player.getUsername(), serverName);
            playerManager().updateOnlinePlayer(akaniPlayer);
        });
    }

    @Subscribe
    public void playerDisconnectHandler(DisconnectEvent event) {
        var player = event.getPlayer();
        playerManager().removeOnlinePlayer(player.getUniqueId());
    }

    public CommonPlayerManager playerManager() {
        return (CommonPlayerManager) corePlugin.core().playerManager();
    }


}
