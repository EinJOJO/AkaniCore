package it.einjojo.akani.core.paper.player;

import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.player.CommonAkaniPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class PaperAkaniPlayer extends CommonAkaniPlayer {

    public PaperAkaniPlayer(InternalAkaniCore core, UUID uuid, String name, String serverName) {
        super(core, uuid, name, serverName);
    }

    @Override
    public void teleport(NetworkLocation networkLocation) {
        bukkitPlayer().ifPresent((bp) -> {

        });
        super.teleport(networkLocation);
    }

    @Override
    public void sendMessage(Component component) {
        bukkitPlayer().ifPresentOrElse((bp) -> {
            bp.sendMessage(component);
        }, () -> {
            //TODO
            core().logger().warning("Tried to send a message to a player that is not online: " + name());
        });
    }


    Optional<Player> bukkitPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uuid()));
    }
}
