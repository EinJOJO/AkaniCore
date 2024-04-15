package it.einjojo.akani.core.player.playtime;

import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;

import java.time.Instant;
import java.util.UUID;

public record CommonPlaytimeHolder(UUID ownerUuid, long playtimeMillis, Instant firstJoin, Instant lastJoin) implements PlaytimeHolder {

}
