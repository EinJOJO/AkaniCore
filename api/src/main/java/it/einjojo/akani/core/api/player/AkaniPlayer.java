package it.einjojo.akani.core.api.player;

import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.api.network.Server;
import net.kyori.adventure.text.Component;

import java.util.concurrent.CompletableFuture;

public interface AkaniPlayer extends AkaniOfflinePlayer{

    String serverName();
    Server server();
    default void connect(Server server) {
        connect(server.name());
    }

    void connect(String servername);

    void teleport(NetworkLocation networkLocation);

    /**
     * @return the player's current location
     */
    CompletableFuture<NetworkLocation> location();

    void sendMessage(Component component);

    @Override
    default boolean isOnline() {
        return true;
    }
}
