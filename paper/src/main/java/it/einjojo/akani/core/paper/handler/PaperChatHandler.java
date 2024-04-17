package it.einjojo.akani.core.paper.handler;

import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.handler.AbstractChatHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Logger;

public class PaperChatHandler extends AbstractChatHandler {
    private final Logger logger;

    public PaperChatHandler(BrokerService brokerService, Logger logger) {
        super(brokerService);
        this.logger = logger;
    }

    @Override
    public String serializeComponent(Component component) {
        return MiniMessage.miniMessage().serialize(component);
    }

    @Override
    public Component deserializeComponent(String serializedComponent) {
        return MiniMessage.miniMessage().deserialize(serializedComponent);
    }

    @Override
    protected void sendMessageLocally(UUID receiver, Component message) {
        Player player = Bukkit.getPlayer(receiver);
        if (player != null) {
            player.sendMessage(message);
        } else {
            logger.warning("Player with UUID " + receiver + " not found. Cannot send message.");
        }

    }

    @Override
    protected void sendMessageToServerLocally(Component message) {
        Bukkit.broadcast(message);
    }
}
