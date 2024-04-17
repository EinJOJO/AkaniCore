package it.einjojo.akani.core.economy;

import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.api.economy.EconomyHolder;

import java.sql.SQLException;
import java.util.UUID;

//TODO
public record EconomyStorage(String tableName, HikariDataSource dataSource) {
    public void seedTables() {
        try (var con = dataSource.getConnection()) {
            try (var ps = con.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName + " (player_uuid VARCHAR(36) PRIMARY KEY, balance BIGINT NOT NULL)")) {
                ps.setString(1, tableName);
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createEconomy(EconomyHolder economyHolder) {
        try (var con = dataSource.getConnection()) {
            try (var ps = con.prepareStatement("INSERT INTO " + tableName + " (player_uuid, balance) VALUES (?, ?)")) {
                ps.setString(1, tableName);
                ps.setString(2, economyHolder.ownerUuid().toString());
                ps.setLong(3, economyHolder.balance());
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public EconomyHolder loadEconomy(UUID uuid) {
        try (var con = dataSource.getConnection()) {
            try (var ps = con.prepareStatement("SELECT * FROM " + tableName + " WHERE player_uuid = ?")) {
                ps.setString(1, tableName);
                ps.setString(2, uuid.toString());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new CommonEconomyHolder(uuid, rs.getLong("balance"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateEconomy(EconomyHolder economyHolder) {
        try (var con = dataSource.getConnection()) {
            try (var ps = con.prepareStatement("UPDATE " + tableName + " SET balance = ? WHERE player_uuid = ?")) {
                ps.setString(1, tableName);
                ps.setLong(2, economyHolder.balance());
                ps.setString(3, economyHolder.ownerUuid().toString());
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
