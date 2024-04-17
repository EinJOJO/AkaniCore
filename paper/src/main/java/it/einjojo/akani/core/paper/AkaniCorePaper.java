package it.einjojo.akani.core.paper;

import it.einjojo.akani.core.AbstractAkaniCore;
import it.einjojo.akani.core.config.YamlConfigFile;
import it.einjojo.akani.core.handler.AbstractChatHandler;
import it.einjojo.akani.core.handler.AbstractCommandHandler;
import it.einjojo.akani.core.handler.AbstractPositionHandler;
import it.einjojo.akani.core.paper.handler.PaperChatHandler;
import it.einjojo.akani.core.paper.handler.PaperCommandHandler;
import it.einjojo.akani.core.paper.handler.PaperPositionHandler;
import it.einjojo.akani.core.paper.player.PaperPlayerFactory;
import it.einjojo.akani.core.player.AkaniPlayerFactory;
import org.bukkit.plugin.java.JavaPlugin;

public class AkaniCorePaper extends AbstractAkaniCore {
    private final AkaniPlayerFactory playerFactory;
    private final AbstractChatHandler chatHandler;
    private final AbstractCommandHandler commandHandler;
    private final AbstractPositionHandler positionHandler;

    protected AkaniCorePaper(JavaPlugin plugin, YamlConfigFile yamlConfigFile) {
        super(plugin.getLogger(), yamlConfigFile.redisCredentials(), yamlConfigFile.mariaDBConfig());
        playerFactory = new PaperPlayerFactory(this);
        chatHandler = new PaperChatHandler(brokerService(), logger());
        commandHandler = new PaperCommandHandler(brokerService(), logger());
        positionHandler = new PaperPositionHandler(plugin, brokerService(), gson());

    }


    @Override
    public AbstractCommandHandler commandHandler() {
        return commandHandler;
    }

    @Override
    public AbstractPositionHandler positionHandler() {
        return positionHandler;
    }

    @Override
    public AkaniPlayerFactory playerFactory() {
        return playerFactory;
    }

    @Override
    public AbstractChatHandler chatHandler() {
        return chatHandler;
    }


}
