package it.einjojo.akani.core.message;

import com.zaxxer.hikari.HikariDataSource;
import it.einjojo.akani.core.api.message.MessageStorage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public record CommonMessageStorage(HikariDataSource dataSource) implements MessageStorage {
    private static final String TABLE_NAME = "core_messages";

    public void init() {
        try (var con = dataSource.getConnection()) {
            con.createStatement().execute("CREATE TABLE IF NOT EXISTS %s (lang VARCHAR(5), message_key VARCHAR(100), message TEXT, PRIMARY KEY (lang, message_key));".formatted(TABLE_NAME));
            if (!isRegistered("de", "prefix")) {
                registerMessage("de", "prefix", "<gray>[<gold>Akani</gold>]</gray>");
            }
            if (!isRegistered("en", "prefix")) {
                registerMessage("en", "prefix", "<gray>[<gold>Akani</gold>]</gray>");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error seeding tables", e);
        }
    }


    @Override
    public Map<String, String> readMessages(String languageKey) {
        try (var con = dataSource.getConnection()) {
            try (var ps = con.prepareStatement("SELECT * FROM %s WHERE lang = ?".formatted(TABLE_NAME))) {
                ps.setString(1, languageKey);
                var rs = ps.executeQuery();
                var messages = new HashMap<String, String>();
                while (rs.next()) {
                    messages.put(rs.getString("message_key"), rs.getString("message"));
                }
                rs.close();
                return messages;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading messages", e);
        }
    }

    @Override
    public boolean isRegistered(String languageKey, String messageKey) {
        try (var con = dataSource.getConnection()) {
            try (var ps = con.prepareStatement("SELECT message_key FROM %s WHERE lang = ? AND message_key = ?".formatted(TABLE_NAME))) {
                ps.setString(1, languageKey);
                ps.setString(2, messageKey);
                var rs = ps.executeQuery();
                boolean result = rs.next();
                rs.close();
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if message is registered", e);
        }
    }

    @Override
    public String readMessage(String lang, String key) {
        try (var con = dataSource.getConnection()) {
            try (var ps = con.prepareStatement("SELECT message FROM %s WHERE lang = ? AND message_key = ?".formatted(TABLE_NAME))) {
                ps.setString(1, lang);
                ps.setString(2, key);
                var rs = ps.executeQuery();
                String result = null;
                if (rs.next()) {
                    result = rs.getString("message");
                }
                rs.close();
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading message", e);
        }
    }

    @Override
    public void registerMessage(String languageKey, String messageKey, String message) {
        try (var con = dataSource.getConnection()) {
            try (var ps = con.prepareStatement("INSERT INTO %s (lang, message_key, message) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE lang=?  ".formatted(TABLE_NAME))) {
                ps.setString(1, languageKey);
                ps.setString(2, messageKey);
                ps.setString(3, message);
                ps.setString(4, languageKey);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving message", e);
        }

    }
}
