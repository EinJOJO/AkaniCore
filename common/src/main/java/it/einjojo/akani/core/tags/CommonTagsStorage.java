package it.einjojo.akani.core.tags;

import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagHolder;
import it.einjojo.akani.core.api.tags.TagRarity;
import it.einjojo.akani.core.api.tags.TagStorage;
import it.einjojo.akani.core.api.util.HikariDataSourceProxy;
import it.einjojo.akani.core.handler.permission.PermissionCheckHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class CommonTagsStorage implements TagStorage {
    private static final Logger log = LoggerFactory.getLogger(CommonTagsStorage.class);
    private final String tagsTableName;
    private final String tagHolderTableName;
    private final PermissionCheckHandler permissionCheckHandler;
    private final HikariDataSourceProxy dataSourceProxy;
    private final CommonTagFactory tagFactory;
    private final String tablePrefix;
    private @NotNull List<Tag> lastLoadedTags = List.of();
    private boolean initSuccessful;

    public CommonTagsStorage(PermissionCheckHandler permissionCheckHandler, HikariDataSourceProxy dataSourceProxy, CommonTagFactory tagFactory, String tablePrefix) {
        this.permissionCheckHandler = permissionCheckHandler;
        this.dataSourceProxy = dataSourceProxy;
        this.tagFactory = tagFactory;
        this.tablePrefix = tablePrefix;
        this.tagsTableName = withPrefix("tags");
        this.tagHolderTableName = withPrefix("tag_holders");
    }

    public String withPrefix(String tableName) {
        return tablePrefix + tableName;
    }

    public void init() {
        try (Connection connection = dataSourceProxy.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS %s (
                        id VARCHAR(36) PRIMARY KEY,
                        display_text VARCHAR(255) NOT NULL,
                        rarity VARCHAR(16) NOT NULL,
                        lore TEXT NOT NULL
                    )
                    """.formatted(tagsTableName));
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS %s (
                        uuid VARCHAR(36) PRIMARY KEY,
                        selected_tag VARCHAR(36),
                        FOREIGN KEY (selected_tag) REFERENCES %s(id)
                    )
                    """.formatted(tagHolderTableName, tagsTableName));
            initSuccessful = true; // Ensures that all tables are available.
            loadTags();
        } catch (SQLException ex) {
            throw new StorageException("init", "all", null, ex);
        }
    }

    @Override
    public @NotNull Collection<Tag> loadTags() {
        checkInit();
        try (ResultSet resultSet = dataSourceProxy.prepareStatement("SELECT * FROM %s".formatted(tagsTableName), ps -> {
        })) {
            List<Tag> tags = new LinkedList<>();
            while (resultSet.next()) {
                Tag tag = parseTag(resultSet);
                if (tag != null) {
                    tags.add(tag);
                }
            }
            lastLoadedTags = tags;
            return tags;
        } catch (SQLException exception) {
            throw new StorageException("load tags", "all", null, exception);
        }
    }

    public @Nullable Tag parseTag(ResultSet rs) {
        checkInit();
        try {
            String id = rs.getString("id");
            String nameMiniMessage = rs.getString("display_text");
            String lore = rs.getString("lore");
            TagRarity rarity = TagRarity.valueOf(rs.getString("rarity"));
            return tagFactory.createTag(id, nameMiniMessage, rarity, lore);
        } catch (Exception ex) {
            log.error("Failed to parse tag from rs: {}", rs, ex);
            return null;
        }
    }

    @Override
    public @Nullable Tag loadTag(@NotNull String id) {
        checkInit();
        try (ResultSet rs = dataSourceProxy.prepareStatement("SELECT * FROM %s WHERE id = ?".formatted(tagsTableName), ps -> {
            try {
                ps.setString(1, id);
            } catch (SQLException e) {
                throw new StorageException("load tag", id, "prepared statement", e);
            }
        })) {
            if (rs.next()) {
                return parseTag(rs);
            }
            return null;
        } catch (SQLException ex) {
            throw new StorageException("load tag", id, null, ex);
        }
    }

    @Override
    public void saveTag(@NotNull Tag tag) {
        checkInit();
        try (ResultSet rs = dataSourceProxy.prepareStatement("INSERT INTO %s (id, display_text, rarity, lore) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE display_text = ?, rarity = ?, lore = ?".formatted(tagsTableName), ps -> {
            try {
                ps.setString(1, tag.id());
                ps.setString(2, tag.plainText());
                ps.setString(3, tag.rarity().name());
                ps.setString(4, tag.lore());
                ps.setString(5, tag.plainText());
                ps.setString(6, tag.rarity().name());
                ps.setString(7, tag.lore());
            } catch (SQLException e) {
                throw new StorageException("save tag", tag.id(), "prepared statement", e);
            }
        })) {
            log.debug(rs.toString());
        } catch (SQLException ex) {
            throw new StorageException("save tag", tag.id(), null, ex);
        }
    }

    @Override
    public void deleteTag(@NotNull String id) {
        checkInit();
        try (ResultSet rs = dataSourceProxy.prepareStatement("DELETE FROM %s WHERE id = ?".formatted(tagsTableName), ps -> {
            try {
                ps.setString(1, id);
            } catch (SQLException e) {
                throw new StorageException("delete tag", id, "prepared statement", e);
            }
        })) {
            if (!rs.next()) {
                throw new StorageException("delete tag", id, "no rows affected", null);
            }
        } catch (SQLException ex) {
            throw new StorageException("delete tag", id, null, ex);
        }
    }

    public static class StorageException extends RuntimeException {

        public StorageException(String failedAction, String identifier, @Nullable String reason, Throwable cause) {
            super(MessageFormat.format("Failed to {0}: id={1} {2}",
                            failedAction,
                            identifier,
                            (reason != null ? "- reason=(" + reason + ")" : "")),
                    cause);
        }
    }

    public String loadSelectedTagId(UUID uuid) {
        checkInit();
        try (ResultSet rs = dataSourceProxy.prepareStatement("SELECT selected_tag FROM %s WHERE uuid = ?".formatted(tagHolderTableName), ps -> {
            try {
                ps.setString(1, uuid.toString());
            } catch (SQLException e) {
                throw new StorageException("load selected tag id", uuid.toString(), "prepared statement", e);
            }
        })) {
            if (rs.next()) {
                return rs.getString("selected_tag");
            }
            return null;
        } catch (SQLException ex) {
            throw new StorageException("load selected tag id", uuid.toString(), null, ex);
        }
    }

    @Override
    public @Nullable TagHolder loadTagHolder(UUID uuid) {
        List<Tag> unlocked = new LinkedList<>();
        Tag selected = null;
        String selectedTagId = loadSelectedTagId(uuid);
        for (Tag tag : lastLoadedTags) {
            if (permissionCheckHandler.hasPermission(uuid, tag.permission())) {
                unlocked.add(tag);
                if (tag.id().equals(selectedTagId)) {
                    selected = tag;
                }
            }
        }
        return new CommonTagHolder(uuid, unlocked, selected);
    }

    @Override
    public void saveTagHolder(@NotNull TagHolder tagHolder) {
        checkInit();
        try (ResultSet rs = dataSourceProxy.prepareStatement("INSERT INTO %s (uuid, selected_tag) VALUES (?, ?) ON DUPLICATE KEY UPDATE selected_tag = ?".formatted(tagHolderTableName), ps -> {
            try {
                Tag selectedTag = tagHolder.selectedTag();
                String tagId = selectedTag != null ? selectedTag.id() : null;
                ps.setString(1, tagHolder.uuid().toString());
                ps.setString(2, tagId);
                ps.setString(3, tagId);
            } catch (SQLException e) {
                throw new StorageException("save tag holder", tagHolder.uuid().toString(), "prepared statement", e);
            }
        })) {
            log.debug(rs.toString());
        } catch (SQLException ex) {
            throw new StorageException("save tag holder", tagHolder.uuid().toString(), null, ex);
        }
    }

    /**
     * @return Cached loaded tags
     */
    @NotNull
    public List<Tag> lastLoadedTags() {
        return lastLoadedTags;
    }

    private void checkInit() {
        if (!initSuccessful) {
            throw new IllegalStateException("Not initialized");
        }
    }
}
