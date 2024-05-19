package it.einjojo.akani.core.paper.scoreboard.defaults;

import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.message.AkaniMessageKey;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.scoreboard.PlayerScoreboard;
import it.einjojo.akani.core.paper.scoreboard.ScoreboardProvider;
import it.einjojo.akani.core.paper.util.TextUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class PlotworldScoreboardProvider implements ScoreboardProvider {

    private final AkaniCore core;

    public PlotworldScoreboardProvider(AkaniCore core) {
        this.core = core;
    }

    @Override
    public boolean shouldProvide(Player player) {
        return player.getWorld().getName().equals("Plotwelt");
    }

    @Override
    public short priority() {
        return PRIORITY.NORMAL;

    }

    @Override
    public void updateScoreboard(PlayerScoreboard sb) {
        AkaniPlayer akaniPlayer = core.playerManager().onlinePlayer(sb.getPlayer().getUniqueId()).orElse(null);
        if (akaniPlayer == null) {
            return;
        }
        Player bukkitPlayer = sb.getPlayer();
        String plotOwner = placeholders(bukkitPlayer, "%plotsquared_currentplot_owner%");
        sb.updateTitle(core.messageManager().message(AkaniMessageKey.SCOREBOARD_TITLE));
        List<Component> content = new LinkedList<>();
        content.add(ScoreboardDefaults.BAR);
        content.addAll(ScoreboardDefaults.playerSection(akaniPlayer));
        content.add(deserialize(TextUtil.transformAmpersandToMiniMessage(placeholders(bukkitPlayer, "    &8▪ &7ᴘʟᴏᴛꜱ: &f%plotsquared_plot_count%&8/&7%plotsquared_allowed_plot_count%"))));
        content.addAll(List.of(
                Component.empty(),
                deserialize("   <#f8c1a1><b>ᴘʟᴏᴛɪɴꜰᴏ"),
                deserialize("    <dark_gray>▪ <gray>ᴘᴏꜱɪᴛɪᴏɴ: <gray>" + (plotOwner.isBlank() ? "-" : placeholders(bukkitPlayer, "%plotsquared_currentplot_xy%"))),
                deserialize("    <dark_gray>▪ <gray>ɪɴʜᴀʙᴇʀ: <gray>" + (plotOwner.isBlank() || plotOwner.contains(" ") ? "-" : TextUtil.transformAmpersandToMiniMessage(plotOwner))),
                Component.empty(),
                deserialize("   <#f8c1a1><b>ᴛɪᴘᴘꜱ")
        ));

        if (placeholders(bukkitPlayer, "%plotsquared_has_plot%").equals("false")) {
            content.add(deserialize("    <dark_gray>▪ <yellow>Nutze /p auto"));
        } else {
            content.add(deserialize("    <dark_gray>▪ <gray>Materialien findest du"));
            content.add(deserialize("    <dark_gray>▪ <gray>auf <red>Dungeons</red>!"));
        }
        content.add(ScoreboardDefaults.BAR);
        sb.updateLines(content);
    }

    protected String placeholders(Player player, String s) {
        return PlaceholderAPI.setPlaceholders(player, s);
    }

    protected Component deserialize(String message) {
        return ScoreboardDefaults.deserialize(message);
    }


}
