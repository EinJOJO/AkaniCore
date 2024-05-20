package it.einjojo.akani.core.paper.home;

import it.einjojo.akani.core.api.home.Home;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.PaperAkaniCore;
import it.einjojo.akani.core.paper.event.AsyncPlayerTeleportHomeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public record PaperHome(UUID owner, String name, NetworkLocation location,
                        PaperAkaniCore core) implements Home {
    @Override
    public void teleport(Object player) {
        Player bukkitPlayer;
        AkaniPlayer akaniPlayer;
        if (player instanceof Player) {
            bukkitPlayer = (Player) player;
            akaniPlayer = core.playerManager().onlinePlayer(bukkitPlayer.getUniqueId()).orElseThrow();
        } else if (player instanceof AkaniPlayer) {
            akaniPlayer = (AkaniPlayer) player;
            bukkitPlayer = Bukkit.getPlayer(akaniPlayer.uuid());
        } else {
            throw new IllegalArgumentException("player is not a valid player");
        }
        if (bukkitPlayer == null) {
            throw new IllegalArgumentException("player is not online. No Bukkit player found.");
        }
        if (Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTaskAsynchronously(core.plugin(), () -> {
                if (new AsyncPlayerTeleportHomeEvent(bukkitPlayer, this).callEvent()) {
                    akaniPlayer.teleport(location);
                }
            });
        } else {
            if (new AsyncPlayerTeleportHomeEvent(bukkitPlayer, this).callEvent()) {
                akaniPlayer.teleport(location);
            }
        }

    }

}
