package it.einjojo.akani.core.handler;

import java.util.UUID;

public class DummyConnectionHandler implements ConnectionHandler {

    public void connectPlayerToServer(UUID player, String serverName) {
        throw new UnsupportedOperationException("Can not because this is a dummy connection handler!");
    }

    @Override
    public void connectPlayerToGroup(UUID player, String groupName) {
        throw new UnsupportedOperationException("Can not because this is a dummy connection handler!");
    }
}
