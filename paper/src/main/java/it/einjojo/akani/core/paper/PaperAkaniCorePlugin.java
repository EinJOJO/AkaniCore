package it.einjojo.akani.core.paper;

import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.core.api.util.SimpleCloudnetAPI;
import it.einjojo.akani.core.config.YamlConfigFile;
import it.einjojo.akani.core.paper.listener.BackListener;
import it.einjojo.akani.core.paper.listener.ConnectionListener;
import it.einjojo.akani.core.paper.listener.ScoreboardListener;
import it.einjojo.akani.core.paper.scoreboard.AsyncScoreboardUpdateTask;
import it.einjojo.akani.core.paper.scoreboard.ScoreboardManager;
import it.einjojo.akani.core.paper.tags.TagHolderLuckPermsNodeChangeListener;
import it.einjojo.akani.core.paper.vault.VaultCoinsEconomy;
import it.einjojo.akani.core.tags.CommonTagManager;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PaperAkaniCorePlugin extends JavaPlugin {
    private PaperAkaniCore paperAkaniCore;
    private YamlConfigFile yamlConfigFile;
    private List<Closeable> shutdownClosables = new LinkedList<>();

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

        RegisteredServiceProvider<LuckPerms> lpProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (lpProvider == null) {
            getLogger().severe("LuckPerms is not available. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!SimpleCloudnetAPI.isAvailable()) {
            getLogger().warning("Cloudnet-API is not available. Will use random UUIDs for server identification");
        }
        paperAkaniCore = new PaperAkaniCore(this, lpProvider.getProvider(), yamlConfigFile);
        AkaniCoreProvider.register(paperAkaniCore);
        paperAkaniCore.load();
        paperAkaniCore.delayedMessageReload();
        new BackListener(this);
        new ConnectionListener(this);
        new ScoreboardListener(this, paperAkaniCore.scoreboardManager());
        new AsyncScoreboardUpdateTask(paperAkaniCore.scoreboardManager()).start(this);
        shutdownClosables.add(new TagHolderLuckPermsNodeChangeListener(lpProvider.getProvider(), (CommonTagManager) paperAkaniCore.tagManager()));
        getServer().getServicesManager().register(AkaniCore.class, paperAkaniCore, this, ServicePriority.Normal);
        getServer().getServicesManager().register(PaperAkaniCore.class, paperAkaniCore, this, ServicePriority.Normal);
        getServer().getServicesManager().register(ScoreboardManager.class, paperAkaniCore.scoreboardManager(), this, ServicePriority.Normal);
        getServer().getServicesManager().register(HikariDataSource.class, paperAkaniCore.dataSource(), this, ServicePriority.Normal);
        getSLF4JLogger().info("Registerd {} in services manager", HikariDataSource.class.getName());
        setupVault();
    }

    @Override
    public void onDisable() {
        for (Closeable closeable : shutdownClosables) {
            try {
                closeable.close();
            } catch (IOException e) {
                getSLF4JLogger().error("Failed to close resource: {}", e.getMessage());
            }
        }
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
