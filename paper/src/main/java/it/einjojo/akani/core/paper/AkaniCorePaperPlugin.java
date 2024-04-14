package it.einjojo.akani.core.paper;

import it.einjojo.akani.core.api.AkaniCoreProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class AkaniCorePaperPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        AkaniCoreProvider.register(null);
    }
}
