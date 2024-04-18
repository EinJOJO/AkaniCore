package it.einjojo.akani.core.player;

import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record PlayerStorage(String tableName, JedisPool jedisPool, HikariDataSource dataSource,
                            InternalAkaniCore core) {
    private static final String PLAYER_KEY_PREFIX = "akani:player:";

    private Jedis jedis() {
        return jedisPool.getResource();
    }

    private Connection connection() throws SQLException {
        return dataSource.getConnection();
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

    public void updateOnlinePlayer(AkaniPlayer player) {
        try (var jedis = jedis()) {
            var key = PLAYER_KEY_PREFIX + player.uuid().toString();
            jedis.hset(key, "name", player.name());
            jedis.hset(key, "server", player.serverName());
        }
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

    public void removeOnlinePlayer(UUID uuid) {
        try (var jedis = jedis()) {
            jedis.del(PLAYER_KEY_PREFIX + uuid.toString());
        }
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

    public AkaniOfflinePlayer loadOfflinePlayer(UUID uuid) {
        try (var connection = connection()) {
            var statement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            var result = statement.executeQuery();
            if (!result.next()) {
                return null;
            }
            return core.playerFactory().offlinePlayer(UUID.fromString(result.getString("uuid")), result.getString("name"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UUID playerUUIDByName(String name) {
        try (var connection = connection()) {
            var statement = connection.prepareStatement("SELECT uuid FROM " + tableName + " WHERE name = ?");
            statement.setString(1, name);
            var result = statement.executeQuery();
            if (!result.next()) {
                return null;
            }
            return UUID.fromString(result.getString("uuid"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void upsertOfflinePlayer(AkaniOfflinePlayer player) {
        try (var connection = connection()) {
            var statement = connection.prepareStatement("INSERT INTO " + tableName + " (uuid, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = ?");
            statement.setString(1, player.uuid().toString());
            statement.setString(2, player.name());
            statement.setString(3, player.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void seedTables() {
        try (var connection = connection()) {
            var statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(32))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
