package it.einjojo.akani.core.api.player.playtime;

import java.time.Instant;
import java.util.UUID;

public interface PlaytimeHolder {

    UUID ownerUuid();

    long playtimeMillis();

    Instant firstJoin();

    Instant lastJoin();



}
