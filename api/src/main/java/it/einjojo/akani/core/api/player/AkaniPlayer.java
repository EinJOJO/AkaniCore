package it.einjojo.akani.core.api.player;

import it.einjojo.akani.core.api.network.Server;

public interface AkaniPlayer extends AkaniOfflinePlayer{

    String serverName();
    Server server();



    @Override
    default boolean isOnline() {
        return true;
    }
}
