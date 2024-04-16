package it.einjojo.akani.core.handler;

import it.einjojo.akani.core.api.util.SimpleCloudnetAPI;

import java.util.UUID;

public class CloudnetConnectionHandler implements ConnectionHandler {
    private final SimpleCloudnetAPI cloudnetAPI;

    public CloudnetConnectionHandler(SimpleCloudnetAPI cloudnetAPI) {
        this.cloudnetAPI = cloudnetAPI;
    }

    @Override
    public void connectPlayer(UUID player, String serverName) {
        cloudnetAPI.getCloudNetPlayerManager().playerExecutor(player).connect(serverName);
    }
}
