package it.einjojo.akani.core.api.tags;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface TagManager {

    /**
     * @return A list of all loaded tags
     */
    List<Tag> availableTags();

    /**
     * Get the tag holder for the player
     *
     * @param uuid the uuid of the player to get the tag holder for
     * @return the tag holder
     */
    @NotNull
    TagHolder tagHolder(UUID uuid);

    /**
     * Update the tagHolder
     * @param uuid UUID of Holder
     * @param tagHolder Holder instance
     */
    void updateTagHolder(UUID uuid, TagHolder tagHolder);

    @Nullable
    Tag tagById(String id);
}
