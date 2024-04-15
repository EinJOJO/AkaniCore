package it.einjojo.akani.core.player;

import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;

import java.util.UUID;

public interface AkaniPlayerFactory {

    AkaniOfflinePlayer offlinePlayer(UUID playerUuid, String playerName);

    AkaniPlayer player(UUID playerUuid, String playerName, String serverName);

}
