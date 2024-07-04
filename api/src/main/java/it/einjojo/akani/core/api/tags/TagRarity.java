package it.einjojo.akani.core.api.tags;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum TagRarity {
    COMMON(Component.text("Common", NamedTextColor.GRAY)),
    RARE(Component.text("Rare", NamedTextColor.GREEN)),
    EPIC(Component.text("Epic", NamedTextColor.DARK_PURPLE)),
    LEGENDARY(Component.text("Legendary", NamedTextColor.GOLD));
    public final Component displayText;

    TagRarity(Component displayText) {
        this.displayText = displayText;
    }
}
