package it.einjojo.akani.core.api.tags;

import net.kyori.adventure.text.Component;

import java.util.UUID;
import java.util.regex.Pattern;

public interface Tag {
    Pattern ID_PATTERN = Pattern.compile("[a-z0-9_]+");
    String PERMISSION_PREFIX = "akani.core.tags";

    String id();

    String plainText();

    Component displayText();

    String lore();

    TagRarity rarity();

    String permission();

}
