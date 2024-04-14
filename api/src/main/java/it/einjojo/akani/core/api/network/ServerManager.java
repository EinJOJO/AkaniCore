package it.einjojo.akani.core.api.network;

import java.util.List;

public interface ServerManager {
    Server server(String name);
    List<Server> servers();
}
