package it.einjojo.akani.core.api.display;

import java.util.Collection;
import java.util.UUID;

public interface DisplayContainerManager {

    Collection<DisplayContainer> displayContainers();

    DisplayContainer createDisplayContainer();

    DisplayContainer displayContainer(UUID playerUniqueId);

}
