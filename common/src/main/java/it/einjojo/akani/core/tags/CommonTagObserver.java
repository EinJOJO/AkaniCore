package it.einjojo.akani.core.tags;

import it.einjojo.akani.core.api.tags.Tag;
import it.einjojo.akani.core.api.tags.TagHolder;

public interface CommonTagObserver {

    void onChangeTag(TagHolder tagHolder, Tag tag);

}
