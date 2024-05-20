package it.einjojo.akani.core.api.home;

import it.einjojo.akani.core.api.network.NetworkLocation;

import java.util.UUID;

public interface Home {

    UUID owner();

    String name();

    NetworkLocation location();

    /**
     * Teleports the player to the home location
     * Calls an event which can be cancelled
     *
     * @param player the player to teleport (bukkit or akani player)
     */
    void teleport(Object player);

}
