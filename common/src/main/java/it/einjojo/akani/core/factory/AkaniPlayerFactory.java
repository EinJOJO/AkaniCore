package it.einjojo.akani.core.factory;

import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;

public interface AkaniPlayerFactory {

    AkaniOfflinePlayer offlinePlayer();

    AkaniPlayer player();

}
