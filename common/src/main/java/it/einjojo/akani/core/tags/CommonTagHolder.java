package it.einjojo.akani.core.tags;

import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class CommonTagHolder implements TagHolder {
    private final UUID uuid;
    private final List<Tag> tags = new LinkedList<>();
    private @Nullable Tag selectedTag;
    private transient @Nullable CommonTagObserver observer;

    public CommonTagHolder(UUID uuid, Collection<Tag> tags, @Nullable Tag selectedTag) {
        this.uuid = uuid;
        this.selectedTag = selectedTag;
        this.tags.addAll(tags);
    }

    @Nullable
    public CommonTagObserver observer() {
        return observer;
    }

    public void setObserver(@Nullable CommonTagObserver observer) {
        this.observer = observer;
    }

    @Override
    public UUID uuid() {
        return uuid;
    }

    @Override
    public List<Tag> availableTags() {
        return tags;
    }

    @Override
    public @Nullable Tag selectedTag() {
        return selectedTag;
    }

    @Override
    public void setSelectedTag(@Nullable Tag tag) {
        this.selectedTag = tag;
        if (observer != null) {
            observer.onChangeTag(this, tag);
        }
    }

    @Override
    public boolean hasSelectedTag() {
        return selectedTag != null;
    }
}
