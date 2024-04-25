package it.einjojo.akani.core.player;

import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.api.network.Server;
import it.einjojo.akani.core.api.player.AkaniPlayer;
import net.kyori.adventure.text.Component;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Common implementation of {@link AkaniPlayer}
 * Needs to be extended by the platform specific implementation
 */
public abstract class CommonAkaniPlayer extends CommonAkaniOfflinePlayer implements AkaniPlayer {

    private final String serverName;

    public CommonAkaniPlayer(InternalAkaniCore core, UUID uuid, String name, String serverName) {
        super(core, uuid, name);
        this.serverName = serverName;
    }

    @Override
    public void runCommand(String command) {
        core.commandHandler().runCommandAsPlayer(uuid(), serverName(), command);
    }

    @Override
    public void sendMessage(Component component) {
        core.chatHandler().sendMessageToPlayer(uuid(), serverName(), component);
    }

    @Override
    public void connect(String serverName) {
        if (this.serverName.equals(serverName)) {
            return;
        }
        core.connectionHandler().connectPlayerToServer(uuid(), serverName);
    }

    @Override
    public CompletableFuture<Boolean> teleportBack() {
        return core.backService().teleportBackAsync(this);
    }

    @Override
    public void teleport(NetworkLocation networkLocation) {
        core.positionHandler().teleport(this, networkLocation); // Announce before connecting avoiding race conditions
        if (networkLocation.type().equals(NetworkLocation.Type.GROUP) && !server().groupName().equals(networkLocation.referenceName())) {
            connectGroup(networkLocation.referenceName());
        }
        if (networkLocation.type().equals(NetworkLocation.Type.SERVER) && !serverName().equals(networkLocation.referenceName())) {
            connect(networkLocation.referenceName());
        }
    }

    @Override
    public CompletableFuture<NetworkLocation> location() {
        return core.positionHandler().position(uuid(), serverName());
    }


    @Override
    public String toString() {
        return "CommonAkaniPlayer{" +
                "serverName='" + serverName + '\'' +
                ", uuid=" + uuid() +
                ", name='" + name() + '\'' +
                '}';
    }

    @Override
    public void connectGroup(String groupName) {
        core.connectionHandler().connectPlayerToGroup(uuid(), groupName);
    }


    @Override
    public Server server() {
        return core.networkManager().server(serverName).orElseThrow();
    }


    @Override
    public String serverName() {
        return serverName;
    }
}
