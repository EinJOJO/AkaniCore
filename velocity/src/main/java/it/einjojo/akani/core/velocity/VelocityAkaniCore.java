package it.einjojo.akani.core.velocity;

import it.einjojo.akani.core.AbstractAkaniCore;
import it.einjojo.akani.core.config.YamlConfigFile;
import it.einjojo.akani.core.handler.ChatHandler;
import it.einjojo.akani.core.handler.CommandHandler;
import it.einjojo.akani.core.handler.PositionHandler;
import it.einjojo.akani.core.player.AkaniPlayerFactory;
import it.einjojo.akani.core.velocity.handler.VelocityChatHandler;
import it.einjojo.akani.core.velocity.handler.VelocityCommandHandler;
import it.einjojo.akani.core.velocity.handler.VelocityPositionHandler;
import it.einjojo.akani.core.velocity.player.VelocityPlayerFactory;

import java.util.logging.Logger;

public class VelocityAkaniCore extends AbstractAkaniCore {
    private final AkaniPlayerFactory playerFactory;
    private final CommandHandler commandHandler;
    private final ChatHandler chatHandler;
    private final PositionHandler positionHandler;

    /**
     * Called on the plugin's onEnable
     */
    protected VelocityAkaniCore(Logger pluginLogger, YamlConfigFile configFile) {
        super(pluginLogger, configFile.redisCredentials(), configFile.mariaDBConfig());
        this.playerFactory = new VelocityPlayerFactory(this, null);
        this.commandHandler = new VelocityCommandHandler(brokerService(), logger());
        this.chatHandler = new VelocityChatHandler(brokerService());
        this.positionHandler = new VelocityPositionHandler(brokerService(), gson());
    }

    @Override
    public AkaniPlayerFactory playerFactory() {
        return playerFactory;
    }

    @Override
    public CommandHandler commandHandler() {
        return commandHandler;
    }


    @Override
    public ChatHandler chatHandler() {
        return chatHandler;
    }

    @Override
    public PositionHandler positionHandler() {
        return positionHandler;
    }
}
