package it.einjojo.akani.core.paper;

import it.einjojo.akani.core.api.network.NetworkLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class AkaniBukkitAdapter {

    public static Location bukkitLocation(NetworkLocation loc) {
        return new Location(Bukkit.getWorld(loc.worldName()), loc.x(), loc.y(), loc.z(), (float) loc.yaw(), (float) loc.pitch());
    }
}
