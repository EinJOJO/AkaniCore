package it.einjojo.akani.core.api.network;

import java.util.List;

public interface NetworkManager {
    Server server(String name);
    List<Server> servers();
    List<String> groups();
}
