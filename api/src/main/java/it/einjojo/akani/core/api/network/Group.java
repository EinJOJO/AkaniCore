package it.einjojo.akani.core.api.network;

import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Set;

public interface Group {
    String name();

    Set<String> serverNames();

    /**
     * Sends a message to all players on the group
     *
     * @param message the message to send
     */
    void broadcast(Component message);




    void runCommand(String command);

}
