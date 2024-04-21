package it.einjojo.akani.core.paper;

import it.einjojo.akani.core.AbstractAkaniCore;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.message.Language;
import it.einjojo.akani.core.config.YamlConfigFile;
import it.einjojo.akani.core.paper.handler.PaperChatHandler;
import it.einjojo.akani.core.paper.handler.PaperCommandHandler;
import it.einjojo.akani.core.paper.handler.PaperPositionHandler;
import it.einjojo.akani.core.paper.player.PaperPlayerFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class PaperAkaniCore extends AbstractAkaniCore implements AkaniCore {
    private final JavaPlugin plugin;
    private final PaperPlayerFactory playerFactory;
    private final PaperChatHandler chatHandler;
    private final PaperCommandHandler commandHandler;
    private final PaperPositionHandler positionHandler;
    private final PaperMessageManager germanMessageManager;
    private final PaperMessageManager englishMessageManager;
    private BukkitTask messageReloadTask;

    protected PaperAkaniCore(JavaPlugin plugin, YamlConfigFile yamlConfigFile) {
        super(plugin.getLogger(), yamlConfigFile.redisCredentials(), yamlConfigFile.mariaDBConfig());
        this.plugin = plugin;
        playerFactory = new PaperPlayerFactory(this);
        germanMessageManager = new PaperMessageManager(Language.GERMAN, messageStorage());
        englishMessageManager = new PaperMessageManager(Language.ENGLISH, messageStorage());
        chatHandler = new PaperChatHandler(brokerService(), logger());
        commandHandler = new PaperCommandHandler(brokerService(), logger(), plugin);
        positionHandler = new PaperPositionHandler(plugin, brokerService(), gson());
    }

    public JavaPlugin plugin() {
        return plugin;
    }

    @Override
    public PaperMessageManager messageManager() {
        return germanMessageManager;
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
    public void delayedMessageReload() {
        if (messageReloadTask != null && !messageReloadTask.isCancelled()) {
            messageReloadTask.cancel();
        }
        messageReloadTask = plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin(), () -> {
            load();
            germanMessageManager.load();
            englishMessageManager.load();
        }, 20L);
    }
}
