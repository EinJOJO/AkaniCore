package it.einjojo.akani.core.paper.handler;

import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.handler.AbstractCommandHandler;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.logging.Logger;

public class PaperCommandHandler extends AbstractCommandHandler {
    public PaperCommandHandler(BrokerService brokerService, Logger logger) {
        super(brokerService, logger);
    }

    @Override
    protected void runServerCommandLocally(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    @Override
    protected void runPlayerCommandLocally(UUID player, String command) {
        var bukkitPlayer = Bukkit.getPlayer(player);
        if (bukkitPlayer == null) {
            logger.warning("Player with UUID " + player + " not found. Cannot run command.");
            return;
        }
        Bukkit.dispatchCommand(bukkitPlayer, command);
    }
}
