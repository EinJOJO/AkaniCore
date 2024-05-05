package it.einjojo.akani.core.velocity;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.core.config.YamlConfigFile;
import it.einjojo.akani.core.util.LuckPermsHook;
import it.einjojo.akani.core.velocity.player.PlayerListener;
import jakarta.inject.Inject;
import net.luckperms.api.LuckPermsProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(
        authors = {"EinJojo @ kalypzo.de"},
        name = "AkaniCoreVelocity",
        version = "1.2.0",
        url = "https://kalypzo.de",
        description = "The core plugin for the Akani network",
        id = "akani-core",
        dependencies = {@Dependency(id = "luckperms")}

)
public class VelocityAkaniCorePlugin {

    private final ProxyServer proxyServer;
    private final Logger logger;
    private VelocityAkaniCore core;

    @Inject
    public VelocityAkaniCorePlugin(ProxyServer proxyServer, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxyServer = proxyServer;
        this.logger = logger;
        YamlConfigFile config = null;
        try {
            config = new YamlConfigFile(dataDirectory.resolve("config.yml"));
        } catch (IOException e) {
            logger.severe("Failed to load config.yml - Wont do anything!");
            logger.severe(e.getMessage());
            return;
        }
        try {
            core = new VelocityAkaniCore(this, logger, config);
            AkaniCoreProvider.register(core);
        } catch (Exception e) {
            logger.severe("Failed to construct AkaniCoreVelocity: Reason: " + e.getMessage());
        }
    }


    @Subscribe
    public void onEnable(ProxyInitializeEvent event) {
        if (core == null) return;
        logger.info("AkaniCoreVelocity is now loading!");
        try {
            core.setLuckPermsHook(new LuckPermsHook(LuckPermsProvider.get()));
            core.load();
            logger.info("AkaniCoreVelocity loaded!");
            core.delayedMessageReload();
            new PlayerListener(this);
        } catch (Exception e) {
            logger.severe("Failed to load AkaniCoreVelocity");
            logger.severe(e.getMessage());
            AkaniCoreProvider.unregister();
            core = null;
            return;
        }
    }

    public ProxyServer proxyServer() {
        return proxyServer;
    }

    public Logger logger() {
        return logger;
    }

    public VelocityAkaniCore core() {
        return core;
    }

    @Subscribe
    public void onDisable(ProxyShutdownEvent event) {
        if (core == null) return;
        logger.info("AkaniCoreVelocity destructing!");
        try {
            core.unload();
        } catch (Exception e) {
            logger.severe("Failed to unload AkaniCoreVelocity");
            logger.severe(e.getMessage());
        }
        AkaniCoreProvider.unregister();
    }

}
