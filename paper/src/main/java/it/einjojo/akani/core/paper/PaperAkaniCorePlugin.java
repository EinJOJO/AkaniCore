package it.einjojo.akani.core.paper;

import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.core.api.util.SimpleCloudnetAPI;
import it.einjojo.akani.core.config.YamlConfigFile;
import it.einjojo.akani.core.paper.listener.BackListener;
import it.einjojo.akani.core.paper.listener.ConnectionListener;
import it.einjojo.akani.core.paper.listener.ScoreboardListener;
import it.einjojo.akani.core.paper.scoreboard.AsyncScoreboardUpdateTask;
import it.einjojo.akani.core.paper.scoreboard.ScoreboardManager;
import it.einjojo.akani.core.paper.vault.VaultCoinsEconomy;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperAkaniCorePlugin extends JavaPlugin {
    private PaperAkaniCore paperAkaniCore;
    private YamlConfigFile yamlConfigFile;

    public PaperAkaniCore paperAkaniCore() {
        return paperAkaniCore;
    }

    public YamlConfigFile yamlConfigFile() {
        return yamlConfigFile;
    }

    @Override
    public void onEnable() {
        try {
            yamlConfigFile = new YamlConfigFile(getDataFolder().toPath().resolve("config.yml"));
        } catch (Exception e) {
            getLogger().severe("Failed to load config file.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider == null) {
            getLogger().severe("LuckPerms is not available. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!SimpleCloudnetAPI.isAvailable()) {
            getLogger().warning("Cloudnet-API is not available. Will use random UUIDs for server identification");
        }
        paperAkaniCore = new PaperAkaniCore(this, provider.getProvider(), yamlConfigFile);
        AkaniCoreProvider.register(paperAkaniCore);
        paperAkaniCore.load();
        paperAkaniCore.delayedMessageReload();
        new BackListener(this);
        new ConnectionListener(this);
        new ScoreboardListener(this, paperAkaniCore.scoreboardManager());
        new AsyncScoreboardUpdateTask(paperAkaniCore.scoreboardManager()).start(this);
        getServer().getServicesManager().register(AkaniCore.class, paperAkaniCore, this, ServicePriority.Normal);
        getServer().getServicesManager().register(PaperAkaniCore.class, paperAkaniCore, this, ServicePriority.Normal);
        getServer().getServicesManager().register(ScoreboardManager.class, paperAkaniCore.scoreboardManager(), this, ServicePriority.Normal);
        setupVault();
    }

    @Override
    public void onDisable() {
        if (paperAkaniCore != null) {
            paperAkaniCore.unload();
            AkaniCoreProvider.unregister();
        }
    }

    private void setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return;
        getServer().getServicesManager().register(Economy.class, new VaultCoinsEconomy(paperAkaniCore), this, ServicePriority.Normal);
    }
}
