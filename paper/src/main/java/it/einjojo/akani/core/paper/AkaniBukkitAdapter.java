package it.einjojo.akani.core.paper;

import it.einjojo.akani.core.api.network.NetworkLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class AkaniBukkitAdapter {

    public static Location bukkitLocation(NetworkLocation loc) {
        return new Location(Bukkit.getWorld(loc.worldName()), loc.x(), loc.y(), loc.z(), (float) loc.yaw(), (float) loc.pitch());
    }

    public static NetworkLocation.Builder networkLocation(Location loc) {
        return NetworkLocation.builder()
                .worldName(loc.getWorld().getName())
                .x(loc.getX())
                .y(loc.getY())
                .z(loc.getZ())
                .yaw(loc.getYaw())
                .pitch(loc.getPitch());
    }

    public static Optional<Player> bukkitPlayer(UUID uuid) {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }
}
