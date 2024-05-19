package it.einjojo.akani.core.paper.scoreboard;

import fr.mrmicky.fastboard.adventure.FastBoard;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class represents a player's scoreboard in the game.
 * It provides methods to manipulate and retrieve information about the scoreboard.
 */
public class PlayerScoreboard {
    private final FastBoard fastBoard;
    private final Map<String, Object> metadataMap = new HashMap<>();
    private ScoreboardProvider activeProvider;

    /**
     * Constructs a new PlayerScoreboard with the given owner and provider.
     *
     * @param owner    the owner of the scoreboard
     * @param provider the provider for the scoreboard
     */
    public PlayerScoreboard(Player owner, ScoreboardProvider provider) {
        this.activeProvider = provider;
        this.fastBoard = new FastBoard(owner);
    }

    /**
     * Sets a metadata value with the given key.
     *
     * @param key   the key for the metadata
     * @param value the value to set
     */
    public <T> void setMetadata(@NotNull String key, @NotNull T value) {
        metadataMap.put(key, value);
    }

    /**
     * Retrieves a metadata value with the given key.
     *
     * @param key the key for the metadata
     * @return the value associated with the key, or null if no value is set
     */
    public <T> Optional<T> metadata(@NotNull String key) {
        return Optional.ofNullable((T) metadataMap.get(key));
    }

    /**
     * Retrieves a metadata value with the given key, or a default value if no value is set.
     *
     * @param key          the key for the metadata
     * @param defaultValue the default value to return if no value is set
     * @return the value associated with the key, or the default value if no value is set
     */
    public @NotNull <T> T metadata(@NotNull String key, @NotNull T defaultValue) {
        return (T) metadataMap.getOrDefault(key, defaultValue);
    }

    /**
     * Updates the lines of the scoreboard.
     *
     * @param lines the new lines for the scoreboard
     */
    public void updateLines(Component... lines) {
        fastBoard.updateLines(lines);
    }

    /**
     * Updates the lines of the scoreboard.
     *
     * @param lines the new lines for the scoreboard
     */
    public void updateLines(Collection<Component> lines) {
        fastBoard.updateLines(lines);
    }

    /**
     * Retrieves the player associated with the scoreboard.
     *
     * @return the player associated with the scoreboard
     */
    public Player getPlayer() {
        return fastBoard.getPlayer();
    }

    /**
     * Retrieves the ID of the scoreboard.
     *
     * @return the ID of the scoreboard
     */
    public String getId() {
        return fastBoard.getId();
    }

    /**
     * Updates the title of the scoreboard.
     *
     * @param title the new title for the scoreboard
     */
    public void updateTitle(Component title) {
        fastBoard.updateTitle(title);
    }

    /**
     * Retrieves the title of the scoreboard.
     *
     * @return the title of the scoreboard
     */
    public Component getTitle() {
        return fastBoard.getTitle();
    }

    /**
     * Updates the scoreboard using the active provider.
     */
    public void update() {
        activeProvider.updateScoreboard(this);
    }

    /**
     * Retrieves the owner of the scoreboard.
     *
     * @return the owner of the scoreboard
     */
    public Player owner() {
        return fastBoard.getPlayer();
    }

    /**
     * Sets the active provider for the scoreboard.
     *
     * @param activeProvider the new active provider
     */
    protected void setActiveProvider(@NotNull ScoreboardProvider activeProvider) {
        this.activeProvider = activeProvider;
    }

    /**
     * Retrieves the active provider for the scoreboard.
     *
     * @return the active provider for the scoreboard
     */
    public ScoreboardProvider activeProvider() {
        return activeProvider;
    }

    /**
     * Retrieves the FastBoard instance associated with the scoreboard.
     *
     * @return the FastBoard instance associated with the scoreboard
     */
    public FastBoard fastBoard() {
        return fastBoard;
    }
}