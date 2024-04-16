package it.einjojo.akani.core.handler;

import java.util.UUID;

public class DummyConnectionHandler implements ConnectionHandler {

    public void connectPlayer(UUID player, String serverName) {
        throw new UnsupportedOperationException("Can not because this is a dummy connection handler!");
    }

}
