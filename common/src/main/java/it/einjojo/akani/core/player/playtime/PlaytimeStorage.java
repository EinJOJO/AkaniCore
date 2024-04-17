package it.einjojo.akani.core.player.playtime;

import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.api.player.playtime.PlaytimeHolder;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.JedisPool;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public record PlaytimeStorage(String tableName, JedisPool jedisPool, HikariDataSource dataSource) {
    public void seedTables() {
        try (var con = dataSource.getConnection()) {
            try (var ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName + " (player_uuid VARCHAR(36) PRIMARY KEY, playtime BIGINT, first_join TIMESTAMP, last_join TIMESTAMP)")) {
                ps.setString(1, tableName);
                ps.execute();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public @Nullable PlaytimeHolder playtimeHolder(UUID uuid) {
        try (var con = dataSource.getConnection()) {
            try (var ps = con.prepareStatement("SELECT * FROM " + tableName + " WHERE player_uuid = ?")) {
                ps.setString(1, tableName);
                ps.setString(2, uuid.toString());
                var rs = ps.executeQuery();
                if (rs.next()) {
                    return new CommonPlaytimeHolder(UUID.fromString(rs.getString("uuid")), rs.getLong("playtime"), rs.getTimestamp("first_join").toInstant(), rs.getTimestamp("last_join").toInstant());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void createPlaytimeHolder(PlaytimeHolder ph) {
        try (var con = dataSource.getConnection()) {
            try (var ps = con.prepareStatement("INSERT INTO " + tableName + " (player_uuid, playtime, first_join, last_join) VALUES (?, ?, ?, ?)")) {
                ps.setString(1, tableName);
                ps.setString(2, ph.ownerUuid().toString());
                ps.setLong(3, ph.playtimeMillis());
                ps.setTimestamp(4, Timestamp.from(ph.firstJoin()));
                ps.setTimestamp(5, Timestamp.from(ph.lastJoin()));
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePlaytimeHolder(PlaytimeHolder ph) {
        try (var con = dataSource.getConnection()) {
            try (var ps = con.prepareStatement("UPDATE " + tableName + " SET playtime = ?, first_join = ?, last_join = ? WHERE player_uuid = ?")) {
                ps.setString(1, tableName);
                ps.setLong(2, ph.playtimeMillis());
                ps.setTimestamp(3, Timestamp.from(ph.firstJoin()));
                ps.setTimestamp(4, Timestamp.from(ph.lastJoin()));
                ps.setString(5, ph.ownerUuid().toString());
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
