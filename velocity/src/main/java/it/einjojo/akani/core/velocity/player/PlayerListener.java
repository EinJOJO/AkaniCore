package it.einjojo.akani.core.velocity.player;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;
import it.einjojo.akani.core.player.CommonPlayerManager;
import it.einjojo.akani.core.player.playtime.CommonPlaytimeHolder;
import it.einjojo.akani.core.velocity.VelocityAkaniCorePlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlayerListener {
    private final VelocityAkaniCorePlugin corePlugin;
    private final Executor executor = Executors.newCachedThreadPool();

    public PlayerListener(VelocityAkaniCorePlugin corePlugin) {
        this.corePlugin = corePlugin;
        corePlugin.proxyServer().getEventManager().register(corePlugin, this);
    }

    @Subscribe
    public void playerJoin(PostLoginEvent event) {

        executor.execute(() -> {
            var player = event.getPlayer();
            var playerUuid = player.getUniqueId();
            var akaniPlayer = corePlugin.core().playerFactory().offlinePlayer(playerUuid, player.getUsername());
            corePlugin.core().playerStorage().upsertOfflinePlayer(akaniPlayer);
            //setup playtime
            PlaytimeHolder playTime = corePlugin.core().playtimeManager().playtimeHolder(playerUuid);
            if (playTime.ownerUuid() == null) { // if the player has no playtime
                corePlugin.logger().info("Creating playtime for " + player.getUsername());
                corePlugin.core().playtimeManager().createPlaytime(playerUuid);
            } else {
                ((CommonPlaytimeHolder) playTime).lastJoin(Instant.now());
                corePlugin.core().playtimeManager().updatePlaytime(playTime);
            }

            // setup economy
            var optionalCoins = corePlugin.core().coinsEconomyManager().playerEconomy(playerUuid);
            if (optionalCoins.isEmpty()) {
                corePlugin.logger().info("Creating coins economy for " + player.getUsername());
                corePlugin.core().coinsStorage().createEconomy(playerUuid);
            }
            var optionalRubin = corePlugin.core().rubinManager().playerEconomy(playerUuid);
            if (optionalRubin.isEmpty()) {
                corePlugin.logger().info("Creating thaler economy for " + player.getUsername());
                corePlugin.core().thalerStorage().createEconomy(playerUuid);
            }
        });
    }


    @Subscribe
    public void playerConnectHandler(ServerConnectedEvent event) {
        var player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        var serverName = event.getServer().getServerInfo().getName();
        if (serverName == null) return;
        var akaniPlayer = corePlugin.core().playerFactory().player(playerUuid, player.getUsername(), serverName);
        playerManager().updateOnlinePlayer(akaniPlayer);

    }

    @Subscribe
    public void playerDisconnectHandler(DisconnectEvent event) {
        executor.execute(() -> {
            var player = event.getPlayer();
            playerManager().removeOnlinePlayer(player.getUniqueId());
            //update playtime
            CommonPlaytimeHolder pt = (CommonPlaytimeHolder) corePlugin.core().playtimeManager().playtimeHolder(player.getUniqueId());
            Instant lastJoin = pt.lastJoin();
            if (lastJoin == null) return;
            Duration duration = Duration.between(pt.lastJoin(), Instant.now());
            pt.playtimeMillis(pt.playtimeMillis() + duration.toMillis());
            corePlugin.core().playtimeManager().updatePlaytime(pt);
        });
    }

    public CommonPlayerManager playerManager() {
        return (CommonPlayerManager) corePlugin.core().playerManager();
    }


}
