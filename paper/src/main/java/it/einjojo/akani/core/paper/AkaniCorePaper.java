package it.einjojo.akani.core.paper;

import it.einjojo.akani.core.AbstractAkaniCore;
import it.einjojo.akani.core.api.player.playtime.PlaytimeManager;
import it.einjojo.akani.core.handler.AbstractCommandHandler;
import it.einjojo.akani.core.player.AkaniPlayerFactory;
import org.bukkit.plugin.java.JavaPlugin;

public class AkaniCorePaper extends AbstractAkaniCore {

    @Override
    public AbstractCommandHandler commandHandler() {
        return null;
    }

    @Override
    public AkaniPlayerFactory playerFactory() {
        return null;
    }

    protected AkaniCorePaper(JavaPlugin plugin) {
        super(plugin.getLogger());
    }
}
