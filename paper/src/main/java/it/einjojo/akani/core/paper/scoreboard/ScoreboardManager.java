package it.einjojo.akani.core.paper.scoreboard;

import org.bukkit.entity.Player;

import java.util.*;

public class ScoreboardManager {
    private final Map<UUID, PlayerScoreboard> scoreboards = new HashMap<>();
    private final Set<ScoreboardProvider> providers = new TreeSet<>();
    private final ScoreboardProvider defaultScoreboardProvider;

    public ScoreboardManager(ScoreboardProvider defaultScoreboardProvider) {
        this.defaultScoreboardProvider = defaultScoreboardProvider;
        registerProvider(defaultScoreboardProvider);
    }

    public Set<ScoreboardProvider> providers() {
        return Collections.unmodifiableSet(providers);
    }

    public Collection<PlayerScoreboard> scoreboards() {
        return Collections.unmodifiableCollection(scoreboards.values());
    }

    public PlayerScoreboard playerScoreboard(UUID uuid) {
        return scoreboards.get(uuid);
    }

    public void createScoreboard(Player player) {
        PlayerScoreboard scoreboard = new PlayerScoreboard(player, defaultScoreboardProvider);
        scoreboards.put(player.getUniqueId(), scoreboard);
    }

    public void removeScoreboard(UUID uuid) {
        PlayerScoreboard removed = scoreboards.remove(uuid);
        if (removed != null) {
            removed.fastBoard().delete();
        }
    }

    public void registerProvider(ScoreboardProvider provider) {
        providers.add(provider); // TreeSet will automatically sort the providers
    }

    public void unregisterProvider(ScoreboardProvider provider) {
        providers.remove(provider);
    }

    public void updateScoreboards() {
        for (PlayerScoreboard scoreboard : scoreboards.values()) {
            scoreboard.update();
        }
    }

    public void updateActiveProviders() {
        for (PlayerScoreboard scoreboard : scoreboards.values()) {
            for (ScoreboardProvider provider : providers) {
                if (provider.shouldProvide(scoreboard.owner())) {
                    scoreboard.setActiveProvider(provider);
                    break;
                }
            }
        }
    }

}
