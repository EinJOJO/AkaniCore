package it.einjojo.akani.core.api.message;

import net.kyori.adventure.text.Component;

public interface MessageManager {
    String plainMessage(String key);
    Component message(String key);
}
