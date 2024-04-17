package it.einjojo.akani.core.paper.handler;

import com.google.gson.Gson;
import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.handler.AbstractPositionHandler;
import it.einjojo.akani.core.paper.AkaniBukkitAdapter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class PaperPositionHandler extends AbstractPositionHandler implements Listener {
    public PaperPositionHandler(JavaPlugin plugin, BrokerService brokerService, Gson gson) {
        super(brokerService, gson);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean isPlayerOnline(UUID player) {
        return Bukkit.getPlayer(player) != null;
    }

    @EventHandler
    public void onPlayerQuit(PlayerJoinEvent event) {
        var player = event.getPlayer();
        var location = openTeleports.getIfPresent(player.getUniqueId());
        if (location != null) {
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
        throw new IllegalArgumentException("Player with UUID " + uuid + " not found.");
    }

    @Override
    public void teleportLocally(UUID player, NetworkLocation location) {
        var bukkitPlayer = Bukkit.getPlayer(player);
        if (bukkitPlayer != null) {
            bukkitPlayer.teleport(AkaniBukkitAdapter.bukkitLocation(location));
        } else {
            throw new IllegalArgumentException("Player with UUID " + player + " not found.");
        }
    }
}
