package it.einjojo.akani.core.api.tags;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface TagStorage {

    @NotNull
    Collection<Tag> loadTags();

    @Nullable
    Tag loadTag(@NotNull String id);

    void saveTag(@NotNull Tag tag);

    void deleteTag(@NotNull String id);

    @Nullable
    TagHolder loadTagHolder(String uuid);

    void saveTagHolder(@NotNull TagHolder tagHolder);

}
