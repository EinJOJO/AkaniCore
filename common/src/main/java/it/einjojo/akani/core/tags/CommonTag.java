package it.einjojo.akani.core.tags;

import com.google.common.base.Preconditions;
import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagRarity;
import net.kyori.adventure.text.Component;

/**
 * Represents a common tag.
 *
 * @param id
 * @param plainText
 * @param displayText
 * @param rarity
 */
public record CommonTag(
        String id,
        String plainText,
        Component displayText,
        TagRarity rarity
) implements Tag {

    public CommonTag(String id, String plainText, Component displayText, TagRarity rarity) {
        if (ID_PATTERN.matcher(id).matches()) {
            throw new IllegalArgumentException("Invalid tag id: " + id);
        }
        this.id = id;
        this.plainText = plainText;
        this.displayText = displayText;
        this.rarity = rarity;
    }
}
