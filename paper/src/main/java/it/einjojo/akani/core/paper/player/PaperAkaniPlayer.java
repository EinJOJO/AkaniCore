package it.einjojo.akani.core.paper.player;

import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.player.CommonAkaniPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class PaperAkaniPlayer extends CommonAkaniPlayer {

    public PaperAkaniPlayer(InternalAkaniCore core, UUID uuid, String name, String serverName) {
        super(core, uuid, name, serverName);
    }

    public Optional<Player> bukkitPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(uuid()));
    }
}
