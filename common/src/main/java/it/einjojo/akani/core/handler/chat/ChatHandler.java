package it.einjojo.akani.core.handler.chat;

import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface ChatHandler {
    void sendMessageToPlayer(UUID specificPlayer, String playerServer, Component message);

    void sendMessageToServer(String serverName, Component message);

    void sendMessageGlobal(Component message);
}
