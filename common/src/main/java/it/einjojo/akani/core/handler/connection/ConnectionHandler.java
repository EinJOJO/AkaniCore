package it.einjojo.akani.core.handler.connection;

import java.util.UUID;

public interface ConnectionHandler {


    void connectPlayerToServer(UUID player, String serverName);

    void connectPlayerToGroup(UUID player, String groupName);

}
