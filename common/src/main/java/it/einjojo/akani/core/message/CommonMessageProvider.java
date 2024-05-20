package it.einjojo.akani.core.message;

import it.einjojo.akani.core.api.message.AkaniMessageKey;
import it.einjojo.akani.core.api.message.MessageProvider;
import it.einjojo.akani.core.api.message.MessageStorage;

public class CommonMessageProvider implements MessageProvider {
    @Override
    public String providerName() {
        return "AkaniCore";
    }

    @Override
    public boolean shouldInsert(MessageStorage s) {
        return !s.isRegistered("de", AkaniMessageKey.SCOREBOARD_TITLE);
    }

    @Override
    public void insertMessages(MessageStorage s) {
        s.registerMessage("de", AkaniMessageKey.GENERIC_ERROR, "%prefix% <red>Ein Fehler ist aufgetreten!");
        s.registerMessage("de", AkaniMessageKey.SPECIFY_PLAYER, "%prefix% <red>Du musst einen Spieler angeben!");
        s.registerMessage("de", AkaniMessageKey.PLAYER_NOT_FOUND, "%prefix% <red>Der Spieler wurde nicht gefunden!");
        s.registerMessage("de", AkaniMessageKey.PLAYER_NOT_ONLINE, "%prefix% <red>Der Spieler ist nicht online!");
        s.registerMessage("de", AkaniMessageKey.NO_PERMISSION, "%prefix% <red>Du hast keine Berechtigung für diesen Befehl!");
        s.registerMessage("de", AkaniMessageKey.SCOREBOARD_TITLE, "<red><bold>ᴀᴋᴀɴɪ.ɴᴇᴛ <white>ɴᴇᴛᴢᴡᴇʀᴋ");
    }
}
