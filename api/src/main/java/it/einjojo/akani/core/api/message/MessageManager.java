package it.einjojo.akani.core.api.message;

import it.einjojo.akani.core.api.player.AkaniPlayer;
import net.kyori.adventure.text.Component;

/**
 * The message manager for a specific language.
 * @param <PLAYER>
 */
public interface MessageManager<PLAYER> {
    /**
     * Loads the messages from the storage
     */
    void load();

    /**
     * Get the plain message from the key
     */
    String plainMessage(String key);

    /**
     * Get the message from the key
     */
    Component message(String key);

    /**
     * Send a message to the player
     *
     * @param player the player to send the message to
     * @param key    the key of the message
     */
    void sendMessage(PLAYER player, String key);

    default void sendMessage(AkaniPlayer player, String key) {
        player.sendMessage(message(key));
    }

    Language language();



}
