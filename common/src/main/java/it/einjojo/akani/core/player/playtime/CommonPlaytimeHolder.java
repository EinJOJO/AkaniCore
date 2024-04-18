package it.einjojo.akani.core.player.playtime;

import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;

import java.time.Instant;
import java.util.UUID;

public class CommonPlaytimeHolder implements PlaytimeHolder {
    private final UUID ownerUuid;
    private final Instant firstJoin;
    private long playtimeMillis;
    private Instant lastJoin;

    public CommonPlaytimeHolder(UUID ownerUuid, long playtimeMillis, Instant firstJoin, Instant lastJoin) {
        this.firstJoin = firstJoin;
        this.lastJoin = lastJoin;
        this.playtimeMillis = playtimeMillis;
        this.ownerUuid = ownerUuid;
    }

    public void lastJoin(Instant lastJoin) {
        this.lastJoin = lastJoin;
    }

    public void playtimeMillis(long playtimeMillis) {
        this.playtimeMillis = playtimeMillis;
    }

    @Override
    public UUID ownerUuid() {
        return ownerUuid;
    }

    @Override
    public Instant firstJoin() {
        return firstJoin;
    }

    @Override
    public long playtimeMillis() {
        return playtimeMillis;
    }

    @Override
    public Instant lastJoin() {
        return lastJoin;
    }
}
