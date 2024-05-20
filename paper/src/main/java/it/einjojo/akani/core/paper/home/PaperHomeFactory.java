package it.einjojo.akani.core.paper.home;

import com.google.common.base.Preconditions;
import it.einjojo.akani.core.api.home.Home;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.home.HomeFactory;
import it.einjojo.akani.core.paper.PaperAkaniCore;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PaperHomeFactory implements HomeFactory {

    private final PaperAkaniCore akaniCore;

    public PaperHomeFactory(@NotNull PaperAkaniCore akaniCore) {
        this.akaniCore = akaniCore;
    }

    @Override
    public Home createHome(UUID owner, String name, NetworkLocation location) {
        Preconditions.checkNotNull(owner);
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(location);
        Preconditions.checkNotNull(akaniCore.playerManager(), "Player manager is not initialized");
        return new PaperHome(owner, name, location, akaniCore);
    }
}
