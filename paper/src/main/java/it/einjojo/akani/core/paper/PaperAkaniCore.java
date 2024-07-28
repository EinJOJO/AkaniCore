package it.einjojo.akani.core.paper;

import it.einjojo.akani.core.AbstractAkaniCore;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.message.Language;
import it.einjojo.akani.core.config.YamlConfigFile;
import it.einjojo.akani.core.home.HomeFactory;
import it.einjojo.akani.core.paper.handler.PaperChatHandler;
import it.einjojo.akani.core.paper.handler.PaperCommandHandler;
import it.einjojo.akani.core.paper.handler.PaperPositionHandler;
import it.einjojo.akani.core.paper.home.PaperHomeFactory;
import it.einjojo.akani.core.paper.player.PaperPlayerFactory;
import it.einjojo.akani.core.paper.scoreboard.ScoreboardManager;
import it.einjojo.akani.core.paper.scoreboard.defaults.DefaultScoreboardProvider;
import it.einjojo.akani.core.paper.scoreboard.defaults.PlotworldScoreboardProvider;
import it.einjojo.akani.core.util.LuckPermsHook;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.awt.*;

public class PaperAkaniCore extends AbstractAkaniCore implements AkaniCore {
    private final JavaPlugin plugin;
    private final PaperPlayerFactory playerFactory;
    private final PaperChatHandler chatHandler;
    private final PaperCommandHandler commandHandler;
    private final PaperPositionHandler positionHandler;
    private final PaperMessageManager germanMessageManager;
    private final PaperMessageManager englishMessageManager;
    private final LuckPermsHook luckPermsHook;
    private final ScoreboardManager scoreboardManager;
    private BukkitTask messageReloadTask;

    protected PaperAkaniCore(JavaPlugin plugin, LuckPerms luckperms, YamlConfigFile yamlConfigFile) {
        super(plugin.getLogger(), yamlConfigFile.redisCredentials(), yamlConfigFile.mariaDBConfig());
        this.plugin = plugin;
        playerFactory = new PaperPlayerFactory(this);
        germanMessageManager = new PaperMessageManager(Language.GERMAN, miniMessage(), messageStorage());
        englishMessageManager = new PaperMessageManager(Language.ENGLISH, miniMessage(), messageStorage());
        chatHandler = new PaperChatHandler(brokerService(), logger());
        commandHandler = new PaperCommandHandler(brokerService(), logger(), plugin);
        positionHandler = new PaperPositionHandler(plugin, brokerService(), gson());
        luckPermsHook = new LuckPermsHook(luckperms);
        scoreboardManager = new ScoreboardManager(new DefaultScoreboardProvider(this));
        scoreboardManager.registerProvider(new PlotworldScoreboardProvider(this));
    }


    public ScoreboardManager scoreboardManager() {
        return scoreboardManager;
    }

    public JavaPlugin plugin() {
        return plugin;
    }

    @Override
    public PaperMessageManager messageManager() {
        return germanMessageManager;
    }

    @Override
    public void broadcast(Component message) {
        Bukkit.broadcast(message);
    }

    @Override
    public void broadcast(String miniMessage) {
        Bukkit.broadcast(miniMessage().deserialize(miniMessage));
    }

    @Override
    public PaperMessageManager germanMessageManager() {
        return germanMessageManager;
    }

    @Override
    public PaperMessageManager englishMessageManager() {
        return englishMessageManager;
    }

    @Override
    public PaperPlayerFactory playerFactory() {
        return playerFactory;
    }


    @Override
    public PaperChatHandler chatHandler() {
        return chatHandler;
    }

    @Override
    public PaperCommandHandler commandHandler() {
        return commandHandler;
    }

    @Override
    public PaperPositionHandler positionHandler() {
        return positionHandler;
    }

    @Override
    public LuckPermsHook luckPermsHook() {

        return luckPermsHook;
    }

    @Override
    public HomeFactory createHomeFactory() {
        return new PaperHomeFactory(this);
    }

    @Override
    public void delayedMessageReload() {
        if (messageReloadTask != null && !messageReloadTask.isCancelled()) {
            messageReloadTask.cancel();
        }
        messageReloadTask = plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin(), () -> {
            loadProviders();
            germanMessageManager.load();
            englishMessageManager.load();
        }, 20L);
    }
}
