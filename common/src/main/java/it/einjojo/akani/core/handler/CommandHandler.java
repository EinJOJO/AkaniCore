package it.einjojo.akani.core.handler;

import java.util.UUID;

public interface CommandHandler {

    /**
     * Run a command as the server.
     *
     * @param serverName Name of the server
     * @param command    Command to run
     */
    void runCommandAsServer(String serverName, String command);
    /**
     * Run a command as a player.
     *
     * @param playerUuid UUID of the player
     * @param serverName Name of the server
     * @param command    Command to run
     */
    void runCommandAsPlayer(UUID playerUuid, String serverName, String command);
}
