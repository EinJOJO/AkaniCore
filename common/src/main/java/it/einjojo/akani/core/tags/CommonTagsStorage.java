package it.einjojo.akani.core.tags;

import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagHolder;
import it.einjojo.akani.core.api.tags.TagStorage;
import it.einjojo.akani.core.handler.permission.PermissionCheckHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class CommonTagsStorage implements TagStorage {
    private final PermissionCheckHandler permissionCheckHandler;

    public CommonTagsStorage(PermissionCheckHandler permissionCheckHandler) {
        this.permissionCheckHandler = permissionCheckHandler;
    }

    @Override
    public @NotNull Collection<Tag> loadTags() {
        return List.of();
    }

    @Override
    public @Nullable Tag loadTag(@NotNull String id) {
        return null;
    }

    @Override
    public void saveTag(@NotNull Tag tag) {

    }

    @Override
    public void deleteTag(@NotNull String id) {

    }

    @Override
    public @Nullable TagHolder loadTagHolder(String uuid) {
        return null;
    }

    @Override
    public void saveTagHolder(@NotNull TagHolder tagHolder) {

    }
}
