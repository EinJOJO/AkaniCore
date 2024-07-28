package it.einjojo.akani.core.paper;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.core.api.util.SimpleCloudnetAPI;
import it.einjojo.akani.core.config.YamlConfigFile;
import it.einjojo.akani.core.paper.command.TagsCommand;
import it.einjojo.akani.core.paper.listener.BackListener;
import it.einjojo.akani.core.paper.listener.ConnectionListener;
import it.einjojo.akani.core.paper.listener.ScoreboardListener;
import it.einjojo.akani.core.paper.player.ClientVersionChecker;
import it.einjojo.akani.core.paper.scoreboard.AsyncScoreboardUpdateTask;
import it.einjojo.akani.core.paper.scoreboard.ScoreboardManager;
import it.einjojo.akani.core.paper.tags.TagHolderLuckPermsNodeChangeListener;
import it.einjojo.akani.core.paper.vault.VaultCoinsEconomy;
import it.einjojo.akani.core.tags.CommonTagManager;
import it.einjojo.akani.util.commands.BukkitMessageFormatter;
import it.einjojo.akani.util.commands.PaperCommandManager;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.print.Paper;
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

        new AsyncScoreboardUpdateTask(paperAkaniCore.scoreboardManager()).start(this);
        loadCommands();
        loadListeners(lpProvider.getProvider());
        ServicesManager servicesManager = getServer().getServicesManager();
        servicesManager.register(AkaniCore.class, paperAkaniCore, this, ServicePriority.Normal);
        servicesManager.register(PaperAkaniCore.class, paperAkaniCore, this, ServicePriority.Normal);
        servicesManager.register(ScoreboardManager.class, paperAkaniCore.scoreboardManager(), this, ServicePriority.Normal);
        servicesManager.register(HikariDataSource.class, paperAkaniCore.dataSource(), this, ServicePriority.Normal);
        getSLF4JLogger().info("Registerd {} in services manager", HikariDataSource.class.getName());
        setupVault();
        setupVersionChecker();
    }

    private void loadCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.setDefaultExceptionHandler((command, registeredCommand, sender, args, t) -> {
            CommandSender commandSender = (CommandSender) sender;
            if (t instanceof IllegalArgumentException) {
                commandSender.sendMessage(paperAkaniCore().miniMessage().deserialize("<red>Ungültige Argumente: <gray>" + t.getMessage()));
                return true;
            }
            commandSender.sendMessage(paperAkaniCore().miniMessage().deserialize("<red>Ein Fehler ist aufgetreten."));
            getSLF4JLogger().error("Error while executing command: {}", command, t);
            return false;
        }, false);
        new TagsCommand(commandManager, paperAkaniCore().tagManager(), paperAkaniCore.miniMessage());
    }


    private void loadListeners(@NotNull LuckPerms luckPerms) {
        new BackListener(this);
        new ConnectionListener(this);
        new ScoreboardListener(this, paperAkaniCore.scoreboardManager());
        shutdownClosables.add(new TagHolderLuckPermsNodeChangeListener(luckPerms, (CommonTagManager) paperAkaniCore.tagManager()));

    }

    @Override
    public void onDisable() {
        for (Closeable closeable : shutdownClosables) {
            try {
                closeable.close();
            } catch (Exception e) {
                getSLF4JLogger().error("Failed to close resource: {} - {}",
                        closeable.getClass().getName(), closeable, e);
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

    private void setupVersionChecker() {
        try {

            new ClientVersionChecker(this, Via.getAPI());
        } catch (NoClassDefFoundError e) {
            getLogger().warning("ViaVersion not found. Skipping version checker.");
        }
    }
}
