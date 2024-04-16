package it.einjojo.akani.core.handler;

import eu.cloudnetservice.modules.bridge.player.executor.ServerSelectorType;
import it.einjojo.akani.core.api.util.SimpleCloudnetAPI;

import java.util.UUID;

public class CloudnetConnectionHandler implements ConnectionHandler {
    private final SimpleCloudnetAPI cloudnetAPI;

    public CloudnetConnectionHandler(SimpleCloudnetAPI cloudnetAPI) {
        this.cloudnetAPI = cloudnetAPI;
    }

    @Override
    public void connectPlayerToServer(UUID player, String serverName) {
        cloudnetAPI.getCloudNetPlayerManager().playerExecutor(player).connect(serverName);
    }

    @Override
    public void connectPlayerToGroup(UUID player, String groupName) {
        cloudnetAPI.getCloudNetPlayerManager().playerExecutor(player).connectToTask(groupName, ServerSelectorType.RANDOM);
    }
}
