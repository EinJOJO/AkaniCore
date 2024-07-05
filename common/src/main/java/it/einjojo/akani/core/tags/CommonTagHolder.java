package it.einjojo.akani.core.tags;

import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagHolder;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CommonTagHolder implements TagHolder {
    private final UUID uuid;
    private final List<Tag> tags = new LinkedList<>();
    private @Nullable Tag selectedTag;
    private transient @Nullable CommonTagHolderObserver observer;

    public CommonTagHolder(UUID uuid, Collection<Tag> tags, @Nullable Tag selectedTag) {
        this.uuid = uuid;
        this.selectedTag = selectedTag;
        this.tags.addAll(tags);
    }

    @Nullable
    public CommonTagHolderObserver observer() {
        return observer;
    }

    public void setObserver(@Nullable CommonTagHolderObserver observer) {
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
            observer.onSelectTag(this, tag);
        }
    }

    @Override
    public boolean hasSelectedTag() {
        return selectedTag != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonTagHolder that = (CommonTagHolder) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
