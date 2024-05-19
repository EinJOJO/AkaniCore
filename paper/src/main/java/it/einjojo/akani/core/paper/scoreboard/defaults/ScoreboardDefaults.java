package it.einjojo.akani.core.paper.scoreboard.defaults;

import it.einjojo.akani.core.api.player.AkaniPlayer;
import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.time.Duration;
import java.util.List;

public class ScoreboardDefaults {
    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    public static final Component BAR = deserialize("  <dark_gray>◆<st>                                  </st>◆  ");

    public static List<Component> playerSection(AkaniPlayer akaniPlayer) {
        return List.of(
                deserialize("   <#f8c1a1><b>ᴘʟᴀʏᴇʀ"),
                deserialize("    <dark_gray>▪ <gray>ʀᴀɴɢ: <white>" + akaniPlayer.plainPrefix().join()),
                deserialize("    <dark_gray>▪ <gray>ᴄᴏɪɴs: <white>" + akaniPlayer.coins().balance()),
                deserialize("    <dark_gray>▪ <gray>ꜱᴘɪᴇʟᴢᴇɪᴛ: <white>" + playtimeString(akaniPlayer))
        );
    }

    public static Component deserialize(String message) {
        return MINI_MESSAGE.deserialize(message);
    }

    public static String playtimeString(AkaniPlayer player) {
        PlaytimeHolder holder = player.playtime();
        long deltaMillis = System.currentTimeMillis() - holder.lastJoin().toEpochMilli();
        long millis = holder.playtimeMillis() + deltaMillis;
        Duration duration = Duration.ofMillis(millis);
        long hours = duration.toHours();
        if (hours <= 2) {
            long minutes = duration.toMinutes();
            if (minutes < 2) {
                return duration.getSeconds() + "s";
            } else {
                return minutes + "m";
            }

        } else {
            return hours + "h";
        }
    }

}
