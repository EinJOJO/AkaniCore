package it.einjojo.akani.core.paper.handler;

import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.handler.command.AbstractCommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.logging.Logger;

public class PaperCommandHandler extends AbstractCommandHandler {
    private final JavaPlugin plugin;

    public PaperCommandHandler(BrokerService brokerService, Logger logger, JavaPlugin plugin) {
        super(brokerService, logger);
        this.plugin = plugin;
    }

    @Override
    protected void runServerCommandLocally(String command) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    @Override
    protected void runPlayerCommandLocally(UUID player, String command) {
        var bukkitPlayer = Bukkit.getPlayer(player);
        if (bukkitPlayer == null) {
            logger.warning("Player with UUID " + player + " not found. Cannot run command.");
            return;
        }
        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.dispatchCommand(bukkitPlayer, command);
        });

    }
}
