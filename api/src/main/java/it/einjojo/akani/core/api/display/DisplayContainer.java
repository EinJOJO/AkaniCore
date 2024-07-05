package it.einjojo.akani.core.api.display;

import java.util.List;

/**
 * This is able to render and hold multiple displays.
 */
public interface DisplayContainer {

    List<DisplayContentProvider> contentProvider();

    void addContentProvider(DisplayContentProvider provider);

    void removeContentProvider(DisplayContentProvider provider);

    void renderContentProviders();

}
