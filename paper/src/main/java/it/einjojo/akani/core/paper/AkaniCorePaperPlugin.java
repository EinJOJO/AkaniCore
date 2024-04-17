package it.einjojo.akani.core.paper;

import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.core.api.util.SimpleCloudnetAPI;
import it.einjojo.akani.core.config.YamlConfigFile;
import it.einjojo.akani.core.paper.vault.VaultCoinsEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class AkaniCorePaperPlugin extends JavaPlugin {
    private AkaniCorePaper akaniCorePaper;
    private YamlConfigFile yamlConfigFile;

    @Override
    public void onEnable() {
        try {
            yamlConfigFile = new YamlConfigFile(getDataFolder().toPath().resolve("config.yml"));
        } catch (Exception e) {
            getLogger().severe("Failed to load config file.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!SimpleCloudnetAPI.isAvailable()) {
            getLogger().warning("Cloudnet-API is not available. Will use random UUIDs for server identification");
        }
        akaniCorePaper = new AkaniCorePaper(this, yamlConfigFile);
        AkaniCoreProvider.register(akaniCorePaper);
        akaniCorePaper.load();
        setupVault();
    }

    @Override
    public void onDisable() {
        if (akaniCorePaper != null) {
            akaniCorePaper.unload();
            AkaniCoreProvider.unregister();
        }
    }

    private void setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return;
        getServer().getServicesManager().register(Economy.class, new VaultCoinsEconomy(), this, ServicePriority.Normal);
    }
}
