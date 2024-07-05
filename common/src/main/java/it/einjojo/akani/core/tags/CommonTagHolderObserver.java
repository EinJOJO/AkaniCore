package it.einjojo.akani.core.tags;

import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagHolder;

public interface CommonTagHolderObserver {

    void onSelectTag(TagHolder tagHolder, Tag tag);


}
