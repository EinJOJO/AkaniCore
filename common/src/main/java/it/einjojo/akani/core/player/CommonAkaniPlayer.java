package it.einjojo.akani.core.player;

import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.api.network.Server;
import it.einjojo.akani.core.api.player.AkaniPlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class CommonAkaniPlayer extends CommonAkaniOfflinePlayer implements AkaniPlayer {

    private final String serverName;

    public CommonAkaniPlayer(InternalAkaniCore core, UUID uuid, String name, String serverName) {
        super(core, uuid, name);
        this.serverName = serverName;
    }


    @Override
    public void connect(String serverName) {
        if (this.serverName.equals(serverName)) {
            return;
        }
        core.playerConnectionHandler().connectPlayer(uuid(), serverName);
    }

    @Override
    public void teleport(NetworkLocation networkLocation) {
        //TODO
    }

    @Override
    public CompletableFuture<NetworkLocation> location() {
        return null;
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
