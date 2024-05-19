package it.einjojo.akani.core.home;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.api.home.Home;
import it.einjojo.akani.core.api.home.HomeHolder;
import it.einjojo.akani.core.api.network.NetworkLocation;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.UUID;

public record CommonHomeStorage(String tablePrefix, HikariDataSource dataSource, Gson gson, HomeFactory homeFactory) {

    private String tableName() {
        return tablePrefix + "_home";
    }

    public boolean deleteHome(UUID homeOwner, String homeName) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM %s WHERE owner = ? AND name = ?".formatted(tableName()))) {
                ps.setString(1, homeOwner.toString());
                ps.setString(2, homeName);
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
            return false;
        }
    }

    public void createTable() {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS %s (owner VARCHAR(36), name VARCHAR(16), location JSON, PRIMARY KEY (owner, name), CHECK(JSON_VALID(location)))".formatted(tableName()))) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }
    }

    public HomeHolder loadHomes(UUID player) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM %s WHERE owner = ?".formatted(tableName()))) {
                ps.setString(1, player.toString());
                var rs = ps.executeQuery();
                var homes = new LinkedList<Home>();
                while (rs.next()) {
                    Home home = homeFactory.createHome(player, rs.getString("name"), networkLocationFromResultSet(rs));
                    homes.add(home);
                }
                rs.close();
                return new CommonHomeHolder(player, homes, this);
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
            return new CommonHomeHolder(player, new LinkedList<>(), this);
        }
    }

    public @Nullable Home loadHome(UUID player, String name) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM %s WHERE owner = ? AND name = ?".formatted(tableName()))) {
                ps.setString(1, player.toString());
                ps.setString(2, name);
                var rs = ps.executeQuery();
                if (rs.next()) {
                    NetworkLocation location = networkLocationFromResultSet(rs);
                    Home home = homeFactory.createHome(player, rs.getString("name"), location);
                    rs.close();
                    return home;
                }
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
        }

        return null;
    }

    private NetworkLocation networkLocationFromResultSet(ResultSet rs) throws SQLException {
        return gson.fromJson(rs.getString("location"), NetworkLocation.class);
    }

    public boolean saveHome(Home home) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO %s (owner, name, location) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE location = ?".formatted(tableName()))) {
                ps.setString(1, home.owner().toString());
                ps.setString(2, home.name());
                ps.setString(3, gson.toJson(home.location()));
                ps.setString(4, gson.toJson(home.location()));
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.fillInStackTrace();
            return false;
        }
    }


}
