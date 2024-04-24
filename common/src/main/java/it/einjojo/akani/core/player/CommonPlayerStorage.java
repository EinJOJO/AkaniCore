package it.einjojo.akani.core.player;

import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.player.AkaniOfflinePlayer;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.ScanParams;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record CommonPlayerStorage(String tableName, JedisPool jedisPool, HikariDataSource dataSource,
                                  InternalAkaniCore core) {

    private static final String PLAYER_KEY_PREFIX = "akani:player:";
    private static final Logger log = LoggerFactory.getLogger(CommonPlayerStorage.class);


    public List<AkaniPlayer> onlinePlayers() {
        try (var jedis = jedisPool.getResource()) {
            var redisKeys = onlinePlayerUuids(jedis);
            var players = new ArrayList<AkaniPlayer>();
            for (var redisKey : redisKeys) {
                var player = onlinePlayer(jedis, redisKey);
                if (player != null) {
                    players.add(player);
                } else {
                    log.warn("Player with uuid {} not found in redis", redisKey);
                }
            }
            return players;
        }
    }

    public @Nullable AkaniPlayer onlinePlayer(UUID uuid) {
        try (var jedis = jedisPool.getResource()) {
            return onlinePlayer(jedis, PLAYER_KEY_PREFIX + uuid.toString());
        }

    }

    public void updateOnlinePlayer(AkaniPlayer player) {
        try (var jedis = jedisPool.getResource()) {
            var key = PLAYER_KEY_PREFIX + player.uuid().toString();
            jedis.hset(key, "name", player.name());
            jedis.hset(key, "server", player.serverName());
        }
    }

    private @Nullable AkaniPlayer onlinePlayer(Jedis jedis, String redisKey) {
        var data = jedis.hgetAll(redisKey);
        if (data.isEmpty()) {
            log.warn("Player with uuid {} is empty.", redisKey);
            return null;
        }
        UUID playerUuid = UUID.fromString(redisKey);
        var playerName = data.get("name");
        var serverName = data.get("server");
        return core.playerFactory().player(playerUuid, playerName, serverName);
    }

    public void removeOnlinePlayer(UUID uuid) {
        log.debug("Removing player with uuid {} from redis", uuid);
        try (var jedis = jedisPool.getResource()) {
            jedis.del(PLAYER_KEY_PREFIX + uuid.toString());
        }
    }


    public List<String> onlinePlayerUuids() {
        try (var jedis = jedisPool.getResource()) {
            return onlinePlayerUuids(jedis);
        }
    }

    private List<String> onlinePlayerUuids(Jedis jedis) {
        var cursor = "0";
        var params = new ScanParams().match(PLAYER_KEY_PREFIX + "*");
        var uuids = new ArrayList<String>();
        do {
            var result = jedis.scan(cursor, params);
            cursor = result.getCursor();
            uuids.addAll(result.getResult());
        } while (!cursor.equals("0"));
        return uuids;
    }

    public AkaniOfflinePlayer loadOfflinePlayer(UUID uuid) {
        try (var connection = dataSource.getConnection()) {
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
        try (var connection = dataSource.getConnection()) {
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
        try (var connection = dataSource.getConnection()) {
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
        try (var connection = dataSource.getConnection()) {
            var statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (uuid VARCHAR(36) PRIMARY KEY, name VARCHAR(32))");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
