package it.einjojo.akani.core.player;

import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record PlayerStorage(String tableName, InternalAkaniCore core) {
    private static final String PLAYER_KEY_PREFIX = "akani:player:";

    private Jedis jedis() {
        return core.jedisPool().getResource();
    }

    private HikariDataSource dataSource() {
        return core.dataSource();
    }

    public List<AkaniPlayer> onlinePlayers() {
        try (var jedis = jedis()) {
            var uuids = onlinePlayerUuids(jedis);
            var players = new ArrayList<AkaniPlayer>();
            for (var uuid : uuids) {
                var player = onlinePlayer(jedis, uuid);
                if (player != null) {
                    players.add(player);
                }
            }
            return players;
        }
    }

    public @Nullable AkaniPlayer onlinePlayer(UUID uuid) {
        return onlinePlayer(jedis(), uuid.toString());
    }

    private @Nullable AkaniPlayer onlinePlayer(Jedis jedis, String uuid) {
        var key = PLAYER_KEY_PREFIX + uuid;
        var data = jedis.hgetAll(key);
        if (data.isEmpty()) {
            return null;
        }
        UUID playerUuid = UUID.fromString(uuid);
        var playerName = data.get("name");
        var serverName = data.get("server");
        return core.playerFactory().player(playerUuid, playerName, serverName);
    }


    public List<String> onlinePlayerUuids() {
        try (var jedis = jedis()) {
            return onlinePlayerUuids(jedis);
        }
    }

    private List<String> onlinePlayerUuids(Jedis jedis) {
        ScanParams scanParams = new ScanParams().match(PLAYER_KEY_PREFIX + "*");
        String cur = ScanParams.SCAN_POINTER_START;
        List<String> keys = new ArrayList<>();
        do {
            ScanResult<String> scanResult = jedis.scan(cur, scanParams);
            keys.addAll(scanResult.getResult());
            cur = scanResult.getCursor();
        } while (!cur.equals(ScanParams.SCAN_POINTER_START));
        return keys;
    }

}
