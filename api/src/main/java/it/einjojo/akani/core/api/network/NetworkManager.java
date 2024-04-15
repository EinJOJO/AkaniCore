package it.einjojo.akani.core.api.network;

import java.util.List;
import java.util.Optional;

public interface NetworkManager {
    Optional<Server> server(String name);
    List<Server> servers();

    Optional<Group> group(String name);
    List<String> groups();
}
