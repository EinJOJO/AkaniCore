package it.einjojo.akani.core.api.tags;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface TagHolder {

    UUID ownerUuid();

    /**
     * Get all available tags for the player
     *
     * @return List of available tags
     */
    List<Tag> availableTags();

    /**
     * Get the selected tag of the player
     *
     * @return Selected tag
     */
    @Nullable
    Tag selectedTag();

    /**
     * Select a tag for the player
     *
     * @param tag Tag to select or null to remove the selected tag
     */
    void setSelectedTag(@Nullable Tag tag);

    /**
     * Check if the player has a selected tag
     *
     * @return true if the player has a selected tag
     */
    boolean hasSelectedTag();


}
