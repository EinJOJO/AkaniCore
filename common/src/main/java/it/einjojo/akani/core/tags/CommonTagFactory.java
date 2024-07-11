package it.einjojo.akani.core.tags;

import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagRarity;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import static it.einjojo.akani.core.api.tags.Tag.ID_PATTERN;

public class CommonTagFactory {

    private final MiniMessage miniMessage;

    public CommonTagFactory(@NotNull MiniMessage miniMessage) {
        this.miniMessage = miniMessage;
    }

    public CommonTag createTag(String id, String displayNameMiniMessage, TagRarity rarity, String lore) {
        if (ID_PATTERN.matcher(id).matches()) {
            throw new IllegalArgumentException("Invalid tag id: " + id);
        }
        return new CommonTag(id, displayNameMiniMessage, miniMessage.deserialize(displayNameMiniMessage), rarity, createPermission(id), lore);
    }

    public String createPermission(String tagId) {
        return Tag.PERMISSION_PREFIX + tagId;
    }

}
