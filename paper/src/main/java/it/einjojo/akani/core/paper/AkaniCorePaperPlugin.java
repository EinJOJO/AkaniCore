package it.einjojo.akani.core.paper;

import it.einjojo.akani.core.api.AkaniCoreProvider;
import it.einjojo.akani.core.paper.vault.VaultCoinsEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class AkaniCorePaperPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        AkaniCoreProvider.register(null);
        setupVault();
    }

    private void setupVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return;
        getServer().getServicesManager().register(Economy.class, new VaultCoinsEconomy(), this, ServicePriority.Normal);
    }
}
