package it.einjojo.akani.core.api.player.playtime;

import java.time.Instant;

public interface PlaytimeHolder {

    long playtimeMillis();

    Instant firstJoin();

    Instant lastJoin();



}
