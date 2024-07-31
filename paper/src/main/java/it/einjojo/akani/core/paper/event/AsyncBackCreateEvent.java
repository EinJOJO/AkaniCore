package it.einjojo.akani.core.paper.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event called when player dies and a back location is about to be created.
 */
public class AsyncBackCreateEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private Location location;
    private boolean cancelled = false;

    public AsyncBackCreateEvent(Player player, Location location) {
        super(true);
        this.player = player;
        this.location = location;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player player() {
        return player;
    }

    public Location location() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public boolean cancelled() {
        return cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
