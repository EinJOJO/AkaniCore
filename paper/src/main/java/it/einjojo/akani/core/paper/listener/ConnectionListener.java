package it.einjojo.akani.core.paper.listener;

import it.einjojo.akani.core.paper.PaperAkaniCorePlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final PaperAkaniCorePlugin plugin;

    public ConnectionListener(PaperAkaniCorePlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        plugin.paperAkaniCore().coinsEconomyManager().playerEconomy(event.getPlayer().getUniqueId()).ifPresent((economyHolder -> {
            plugin.paperAkaniCore().coinsEconomyManager().updateEconomy(economyHolder);
        }));
        plugin.paperAkaniCore().thalerManager().playerEconomy(event.getPlayer().getUniqueId()).ifPresent((economyHolder -> {
            plugin.paperAkaniCore().thalerManager().updateEconomy(economyHolder);
        }));
    }

}
