package it.einjojo.akani.core.paper.listener;

import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.paper.AkaniBukkitAdapter;
import it.einjojo.akani.core.paper.PaperAkaniCorePlugin;
import it.einjojo.akani.core.paper.event.AsyncBackCreateEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BackListener implements Listener {
    private final PaperAkaniCorePlugin plugin;

    public BackListener(PaperAkaniCorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
        if (damageEvent != null && damageEvent.getCause() == EntityDamageEvent.DamageCause.VOID) return;
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            AsyncBackCreateEvent backEvent = new AsyncBackCreateEvent(player, deathLocation);
            plugin.getServer().getPluginManager().callEvent(backEvent);
            if (backEvent.isCancelled()) return;
            NetworkLocation location = AkaniBukkitAdapter.networkLocation(backEvent.location()).referenceName(plugin.paperAkaniCore().serverName()).build();
            plugin.paperAkaniCore().backService().saveBackLocation(backEvent.player().getUniqueId(), location);
        });
    }

}
