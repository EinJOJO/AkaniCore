package it.einjojo.akani.core.velocity.home;

import com.velocitypowered.api.proxy.Player;
import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.home.Home;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.api.player.AkaniPlayer;

import java.util.UUID;

public record VelocityHome(UUID owner, String name, NetworkLocation location, AkaniCore core) implements Home {
    @Override
    public void teleport(Object player) {
        if (player instanceof AkaniPlayer akaniPlayer) {
            akaniPlayer.teleport(location);
        } else if (player instanceof Player velocityPlayer) {
            core.playerManager().onlinePlayer(velocityPlayer.getUniqueId()).ifPresent(akaniPlayer1 -> akaniPlayer1.teleport(location));
        } else {
            throw new IllegalArgumentException("player is not a valid player");
        }
    }
}
