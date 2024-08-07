package it.einjojo.akani.core.paper.handler;

import com.google.gson.Gson;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.handler.position.AbstractPositionHandler;
import it.einjojo.akani.core.paper.AkaniBukkitAdapter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PaperPositionHandler extends AbstractPositionHandler implements Listener {
    private final JavaPlugin plugin;


    public PaperPositionHandler(JavaPlugin plugin, BrokerService brokerService, Gson gson) {
        super(brokerService, gson);
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean isPlayerOnline(UUID player) {
        return Bukkit.getPlayer(player) != null;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        var location = openTeleports.getIfPresent(player.getUniqueId());
        if (location != null) {
            logger().info("Player {} joined and has an open teleport ", player.getName());
            teleportLocally(player.getUniqueId(), location);
            openTeleports.invalidate(player.getUniqueId());
        }
    }

    @Override
    public NetworkLocation positionLocally(UUID uuid) {
        var bukkitPlayer = Bukkit.getPlayer(uuid);
        if (bukkitPlayer != null) {
            return AkaniBukkitAdapter.networkLocation(bukkitPlayer.getLocation()).type(NetworkLocation.Type.SERVER).referenceName(brokerService().brokerName()).build();
        }
        throw new IllegalArgumentException("Position locally failed player not found.");
    }

    @Override
    public void teleportLocally(@NotNull UUID player, @NotNull NetworkLocation location) {
        logger().debug("Teleporting player {} locally to {}", player, location);
        Bukkit.getScheduler().runTask(plugin, () -> {
            var bukkitPlayer = Bukkit.getPlayer(player);
            if (bukkitPlayer != null) {
                bukkitPlayer.teleportAsync(AkaniBukkitAdapter.bukkitLocation(location));
            } else {
                throw new IllegalArgumentException("Teleport locally failed player not found.");
            }
        });
    }
}
