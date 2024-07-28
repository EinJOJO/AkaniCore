package it.einjojo.akani.core.tags;

import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagHolder;
import org.jetbrains.annotations.Nullable;

public interface CommonTagHolderObserver {

    void onSelectTag(TagHolder tagHolder, @Nullable Tag tag);

    void onAddTag(TagHolder tagHolder, Tag added);

}
