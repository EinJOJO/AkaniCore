package it.einjojo.akani.core.paper.scoreboard;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Provides the lines for the scoreboard
 */
public interface ScoreboardProvider extends Comparable<ScoreboardProvider> {

    boolean shouldProvide(Player player);

    /**
     * @return the priority of the provider, higher is better (0 is lowest)
     */
    short priority();

    void updateScoreboard(PlayerScoreboard scoreboard);

    @Override
    default int compareTo(@NotNull ScoreboardProvider o) {
        return Short.compare(o.priority(), priority());
    }

    interface PRIORITY {
        short LOWEST = 1;
        short LOW = 5;
        short NORMAL = 10;
        short HIGH = 20;
        short HIGHEST = Short.MAX_VALUE;
    }
}
