package it.einjojo.akani.core.player;

import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.economy.EconomyHolder;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;
import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagHolder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CommonAkaniOfflinePlayer implements AkaniOfflinePlayer {

    protected final InternalAkaniCore core;
    private final UUID uuid;
    private final String name;

    public CommonAkaniOfflinePlayer(InternalAkaniCore core, UUID uuid, String name) {
        this.core = core;
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public CompletableFuture<Boolean> hasPermissionAsync(String permission) {
        return core.luckPermsHook().hasPlayerPermission(uuid(), permission);
    }

    @Override
    public CompletableFuture<String> plainPrefix() {
        return core.luckPermsHook().prefix(uuid());
    }

    @Override
    public CompletableFuture<Component> prefix() {
        return plainPrefix().thenApply((prefix) -> core.miniMessage().deserialize(prefix));
    }

    @Override
    public CompletableFuture<String> plainSuffix() {
        return core().luckPermsHook().suffix(uuid());
    }

    @Override
    public CompletableFuture<Component> suffix() {
        return plainSuffix().thenApply((suffix) -> core().miniMessage().deserialize(suffix));
    }

    public InternalAkaniCore core() {
        return core;
    }

    @Override
    public UUID uuid() {
        return uuid;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "CommonAkaniOfflinePlayer{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public PlaytimeHolder playtime() {
        return core.playtimeManager().playtimeHolder(uuid());
    }

    @Override
    public EconomyHolder coins() {
        return core.coinsManager().playerEconomy(uuid()).orElseThrow();
    }

    @Override
    public CompletableFuture<PlaytimeHolder> playtimeAsync() {
        return core.playtimeManager().playtimeHolderAsync(uuid());
    }

    @Override
    public CompletableFuture<EconomyHolder> coinsAsync() {
        return core.coinsManager().playerEconomyAsync(uuid()).thenApply(Optional::orElseThrow);
    }

    @Override
    public CompletableFuture<EconomyHolder> thalerAsync() {
        return core.thalerManager().playerEconomyAsync(uuid()).thenApply(Optional::orElseThrow);
    }

    @Override
    public EconomyHolder thaler() {
        return core.thalerManager().playerEconomy(uuid()).orElseThrow();
    }

    public TagHolder tagHolder() {
        return core.tagManager().tagHolder(uuid());
    }

    @Override
    public boolean hasPermission(String permission) {
        return core.permissionCheckHandler().hasPermission(uuid(), permission);
    }

    @Override
    public List<Tag> availableTags() {
        return tagHolder().availableTags();
    }

    @Override
    public @Nullable Tag selectedTag() {
        return tagHolder().selectedTag();
    }

    @Override
    public void addTag(Tag tag) {
        tagHolder().addTag(tag);
    }

    @Override
    public void setSelectedTag(@Nullable Tag tag) {
        tagHolder().setSelectedTag(tag);
    }

    @Override
    public boolean hasSelectedTag() {
        return tagHolder().hasSelectedTag();
    }
}
