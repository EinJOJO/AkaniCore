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
        TagRarity rarity,
        String permission,
        String lore
) implements Tag {}
