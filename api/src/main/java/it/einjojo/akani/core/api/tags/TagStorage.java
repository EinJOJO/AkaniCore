package it.einjojo.akani.core.api.tags;

import java.util.Collection;

public interface TagStorage {

    Collection<Tag> loadTags();

    Tag loadTag(String id);

    void saveTag(Tag tag);

}
