package it.einjojo.akani.core.paper.listener;

import it.einjojo.akani.core.paper.scoreboard.ScoreboardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ScoreboardListener implements Listener {
    private final ScoreboardManager scoreboardManager;


    public ScoreboardListener(JavaPlugin plugin, ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void registerScoreboardOnJoin(PlayerJoinEvent event) {
        scoreboardManager.createScoreboard(event.getPlayer());
    }

    @EventHandler
    public void unregisterScoreboardOnQuit(PlayerQuitEvent event) {
        scoreboardManager.removeScoreboard(event.getPlayer().getUniqueId());
    }


}
