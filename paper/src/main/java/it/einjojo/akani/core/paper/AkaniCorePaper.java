package it.einjojo.akani.core.paper;

import it.einjojo.akani.core.AbstractAkaniCore;
import it.einjojo.akani.core.config.YamlConfigFile;
import it.einjojo.akani.core.handler.AbstractCommandHandler;
import it.einjojo.akani.core.paper.player.PaperPlayerFactory;
import it.einjojo.akani.core.player.AkaniPlayerFactory;
import org.bukkit.plugin.java.JavaPlugin;

public class AkaniCorePaper extends AbstractAkaniCore {
    private final AkaniPlayerFactory playerFactory;

    protected AkaniCorePaper(JavaPlugin plugin, YamlConfigFile yamlConfigFile) {
        super(plugin.getLogger(), yamlConfigFile.redisCredentials(), yamlConfigFile.mariaDBConfig());
        playerFactory = new PaperPlayerFactory(this);
    }



    @Override
    public AbstractCommandHandler commandHandler() {
        return null;
    }

    @Override
    public AkaniPlayerFactory playerFactory() {
        return playerFactory;
    }
}
