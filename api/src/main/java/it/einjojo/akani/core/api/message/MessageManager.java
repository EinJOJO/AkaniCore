package it.einjojo.akani.core.api.message;

import it.einjojo.akani.core.api.player.AkaniPlayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * The message manager for a specific language.
 *
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
    String plainMessage(@NotNull String key);

    String plainMessage(@NotNull String key, @Nullable Function<String, String> modifier);

    /**
     * Get the message from the key
     */
    Component message(@NotNull String key);

    Component message(@NotNull String key, @Nullable Function<String, String> modifier);

    /**
     * Send a message to the player
     *
     * @param player the player to send the message to
     * @param key    the key of the message
     */
    void sendMessage(PLAYER player, String key, @Nullable Function<String, String> modifier);

    void sendMessage(PLAYER player, String key);


    void sendMessage(AkaniPlayer player, String key, @Nullable Function<String, String> modifier);

    void sendMessage(AkaniPlayer player, String key);

    Language language();


}
