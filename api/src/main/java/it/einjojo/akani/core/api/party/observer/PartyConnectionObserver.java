package it.einjojo.akani.core.api.party.observer;

import it.einjojo.akani.core.api.player.AkaniPlayer;

/**
 * Represents an observer that listens for party connections.
 * This observer is notified when a player connects to a party.
 * This observer is notified when a player disconnects from a party.
 * This observer is notified when a player is kicked from a party.
 */
public interface PartyConnectionObserver {

    /**
     * Called when a party player leaves the server
     *
     * @param player The player that was kicked
     */
    void onPartyPlayerLeaveNetwork(AkaniPlayer player);

    /**
     * Called when a party player joins the server (mostly reconnect)
     *
     * @param player The player that joined the party
     */
    void onPartyPlayerJoinNetwork(AkaniPlayer player);

    /**
     * Called when a player that is inside a party changes server.
     *
     * @param player The player that was kicked
     */
    void onPartyPlayerChangeServer(AkaniPlayer player);
}
