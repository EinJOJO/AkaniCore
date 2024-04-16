package it.einjojo.akani.core.network;

import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.network.Group;
import it.einjojo.akani.core.api.network.Server;

public record CommonServer(InternalAkaniCore core, String name, String groupName) implements Server {

    @Override
    public Group group() {
        return core.networkManager().group(groupName).orElseThrow();
    }

    @Override
    public void runCommand(String command) {
        core.commandHandler().runCommandAsServer(name(), command);
    }
}

