package it.einjojo.akani.core.paper.event;

import it.einjojo.akani.core.api.home.Home;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;


public class AsyncPlayerTeleportHomeEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Home home;
    private boolean cancelled;

    public AsyncPlayerTeleportHomeEvent(@NotNull Player who, Home home) {
        super(who, true);
        this.home = home;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Home home() {
        return home;
    }


    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
