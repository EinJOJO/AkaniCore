package it.einjojo.akani.core.api.player;

import it.einjojo.akani.core.api.economy.EconomyHolder;
import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;

import java.util.UUID;

public interface AkaniOfflinePlayer {
    UUID uuid();
    String name();
    boolean isOnline();
    PlaytimeHolder playtime();
    EconomyHolder coins();
    EconomyHolder thaler();

}
