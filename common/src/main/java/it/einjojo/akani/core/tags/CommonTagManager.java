package it.einjojo.akani.core.tags;

import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagHolder;
import it.einjojo.akani.core.api.tags.TagManager;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommonTagManager implements TagManager, CommonTagObserver {
    @Override
    public @NotNull TagHolder tagHolder(UUID uuid) {
        return null;
    }

    @Override
    public void updateTagHolder(UUID uuid, TagHolder tagHolder) {

    }

    @Override
    public void onChangeTag(TagHolder tagHolder, Tag tag) {

    }
}
