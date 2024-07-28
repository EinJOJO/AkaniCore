package it.einjojo.akani.core.paper.player;

import com.google.common.base.Preconditions;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 *
 */
public class ClientVersionChecker implements Listener {
    private final JavaPlugin plugin;
    private final ViaAPI<?> viaApi;

    public ClientVersionChecker(@NotNull JavaPlugin plugin, @NotNull ViaAPI<?> viaApi) {
        Preconditions.checkNotNull(plugin);
        Preconditions.checkNotNull(viaApi);
        this.plugin = plugin;
        this.viaApi = viaApi;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoinServer(PlayerJoinEvent event) {
        if (!isClientVersionSupported(event.getPlayer())) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                sendUnsupportedClientMessage(event.getPlayer());
            }, 20L);
        }
    }

    public boolean isClientVersionSupported(Player player) {
        return viaApi.getPlayerProtocolVersion(player.getUniqueId()).betweenInclusive(ProtocolVersion.v1_18, ProtocolVersion.v1_20_3);
    }

    public void sendUnsupportedClientMessage(Player player) {
        Title title = Title.title(
                Component.text("Inkompatible Version!", NamedTextColor.RED).decorate(TextDecoration.BOLD),
                Component.text("Wir empfehlen die ", NamedTextColor.GRAY).append(Component.text("1.20.4", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)),
                Title.Times.times(Duration.ofMillis(10), Duration.ofSeconds(5), Duration.ofMillis(10))
        );
        player.showTitle(title);
        player.sendMessage(Component.text("Deine Version wird nicht vollständig untertüzt!", NamedTextColor.RED).decorate(TextDecoration.BOLD));
        player.sendMessage(Component.text("Wir empfehlen die ", NamedTextColor.GRAY).append(Component.text("1.20.4", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)));
        player.sendMessage(Component.text("Du kannst trotzdem spielen, aber einige Funktionen werden nicht verfügbar sein.", NamedTextColor.GRAY));
        player.sendMessage(Component.text("Womöglich triffst du auf unsichtbare Monster, weil das Texturepack nicht geladen werden kann.", NamedTextColor.GRAY));
    }
}
