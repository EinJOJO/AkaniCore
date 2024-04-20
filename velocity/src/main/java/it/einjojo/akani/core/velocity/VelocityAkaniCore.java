package it.einjojo.akani.core.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import it.einjojo.akani.core.AbstractAkaniCore;
import it.einjojo.akani.core.api.message.Language;
import it.einjojo.akani.core.config.YamlConfigFile;
import it.einjojo.akani.core.velocity.handler.VelocityChatHandler;
import it.einjojo.akani.core.velocity.handler.VelocityCommandHandler;
import it.einjojo.akani.core.velocity.handler.VelocityPositionHandler;
import it.einjojo.akani.core.velocity.player.VelocityPlayerFactory;

import java.time.Duration;
import java.util.logging.Logger;

public class VelocityAkaniCore extends AbstractAkaniCore {
    private final VelocityAkaniCorePlugin plugin;
    private final VelocityPlayerFactory playerFactory;
    private final VelocityCommandHandler commandHandler;
    private final VelocityChatHandler chatHandler;
    private final VelocityPositionHandler positionHandler;
    private final VelocityMessageManager germanMessageManager;
    private final VelocityMessageManager englishMessageManager;
    private ScheduledTask messageReloadTask;

    /**
     * Called on the plugin's onEnable
     */
    protected VelocityAkaniCore(VelocityAkaniCorePlugin plugin, Logger pluginLogger, YamlConfigFile configFile) {
        super(pluginLogger, configFile.redisCredentials(), configFile.mariaDBConfig());
        this.plugin = plugin;
        this.playerFactory = new VelocityPlayerFactory(this, null);
        this.commandHandler = new VelocityCommandHandler(brokerService(), logger());
        this.chatHandler = new VelocityChatHandler(brokerService());
        this.positionHandler = new VelocityPositionHandler(brokerService(), gson());
        this.germanMessageManager = new VelocityMessageManager(Language.GERMAN, messageStorage());
        this.englishMessageManager = new VelocityMessageManager(Language.ENGLISH, messageStorage());
    }


    public VelocityAkaniCorePlugin plugin() {
        return plugin;
    }

    public ProxyServer proxyServer() {
        return plugin.proxyServer();
    }

    @Override
    public VelocityPlayerFactory playerFactory() {
        return playerFactory;
    }

    @Override
    public VelocityCommandHandler commandHandler() {
        return commandHandler;
    }

    @Override
    public VelocityChatHandler chatHandler() {
        return chatHandler;
    }

    @Override
    public VelocityPositionHandler positionHandler() {
        return positionHandler;
    }

    @Override
    public void delayedMessageReload() {
        if (messageReloadTask != null) {
            messageReloadTask.cancel();
            messageReloadTask = null;
        }
        messageReloadTask = proxyServer().getScheduler().buildTask(plugin, () -> {
            loadProviders();
            germanMessageManager.load();
            englishMessageManager.load();
        }).delay(Duration.ofSeconds(2)).schedule();


    }

    @Override
    public VelocityMessageManager germanMessageManager() {
        return germanMessageManager;
    }

    @Override
    public VelocityMessageManager englishMessageManager() {
        return englishMessageManager;
    }
}
