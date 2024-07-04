package it.einjojo.akani.core.api.tags;

import net.kyori.adventure.text.Component;

public interface Tag {
    String id();

    String plainText();

    Component displayText();

    TagRarity rarity();
}
