package it.einjojo.akani.core.api.tags;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface TagManager {

    /**
     * Get the tag holder for the player
     *
     * @param uuid the uuid of the player to get the tag holder for
     * @return the tag holder
     */
    @NotNull
    TagHolder tagHolder(UUID uuid);

    void updateTagHolder(UUID uuid, TagHolder tagHolder);

}
