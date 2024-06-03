package it.einjojo.akani.core.paper.scoreboard;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class AsyncScoreboardUpdateTask implements Runnable {
    public static final int UPDATE_SPEED = 15;
    private final ScoreboardManager scoreboardManager;
    private BukkitTask task;

    public AsyncScoreboardUpdateTask(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    public void start(JavaPlugin plugin) {
        stop();
        task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0, UPDATE_SPEED);
    }

    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public void run() {
        scoreboardManager.updateActiveProviders();
        scoreboardManager.updateScoreboards();
    }
}
