package it.einjojo.akani.core.velocity.handler;

import it.einjojo.akani.core.api.messaging.BrokerService;
import it.einjojo.akani.core.handler.chat.AbstractChatHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.UUID;

public class VelocityChatHandler extends AbstractChatHandler {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public VelocityChatHandler(BrokerService brokerService) {
        super(brokerService);
    }

    @Override
    public String serializeComponent(Component component) {
        return miniMessage.serialize(component);
    }

    @Override
    public Component deserializeComponent(String serializedComponent) {
        return miniMessage.deserialize(serializedComponent);
    }

    @Override
    protected void sendMessageLocally(UUID receiver, Component message) {
        return;
    }

    @Override
    protected void sendMessageToServerLocally(Component message) {
        return;
    }
}
