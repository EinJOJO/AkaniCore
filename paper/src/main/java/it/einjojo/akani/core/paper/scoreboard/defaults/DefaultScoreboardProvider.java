package it.einjojo.akani.core.paper.scoreboard.defaults;

import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.message.AkaniMessageKey;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.paper.PaperAkaniCore;
import it.einjojo.akani.core.paper.PaperMessageManager;
import it.einjojo.akani.core.paper.scoreboard.PlayerScoreboard;
import it.einjojo.akani.core.paper.scoreboard.ScoreboardProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class DefaultScoreboardProvider implements ScoreboardProvider {
    private final PaperAkaniCore core;

    public DefaultScoreboardProvider(PaperAkaniCore core) {
        this.core = core;
    }

    @Override
    public boolean shouldProvide(Player player) {
        return true; // always provide the default scoreboard
    }

    @Override
    public short priority() {
        return 0;
    }

    @Override
    public void updateScoreboard(PlayerScoreboard sb) {
        AkaniPlayer akaniPlayer = core.playerManager().onlinePlayer(sb.getPlayer().getUniqueId()).orElse(null);
        if (akaniPlayer == null) {
            return;
        }

        sb.updateTitle(messageManager().message(AkaniMessageKey.SCOREBOARD_TITLE));
        List<Component> list = new LinkedList<>();
        list.add(ScoreboardDefaults.BAR);
        list.addAll(ScoreboardDefaults.playerSection(akaniPlayer));
        list.addAll(List.of(
                Component.empty(),
                deserialize("   <#f8c1a1><b>ꜱᴇʀᴠᴇʀ"),
                deserialize("    <dark_gray>▪ <gray>ɴᴀᴍᴇ: <white>" + core.serverName()),
                deserialize("    <dark_gray>▪ <gray>ᴏɴʟɪɴᴇ: <white>" + Bukkit.getServer().getOnlinePlayers().size()),
                deserialize("    <dark_gray>▪ <gray>ᴛᴘs: <white>" + "%.2f".formatted(Bukkit.getServer().getTPS()[0])),
                Component.empty(),
                deserialize("   <#f8c1a1><b>ᴇᴠᴇɴᴛꜱ"),
                deserialize("    <dark_gray>▪ <red>ɴᴏɴᴇ"),
                ScoreboardDefaults.BAR
        ));
        sb.updateLines(list);

    }


    protected Component deserialize(String message) {
        return ScoreboardDefaults.deserialize(message);
    }

    protected PaperMessageManager messageManager() {
        return core.messageManager();
    }
}
