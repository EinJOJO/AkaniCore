package it.einjojo.akani.core.network;

import com.google.common.collect.ImmutableSet;
import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.network.Group;
import net.kyori.adventure.text.Component;

import java.util.HashSet;
import java.util.Set;

public class CommonGroup implements Group {
    private final String name;
    private final HashSet<String> servers = new HashSet<>();
    private final InternalAkaniCore core;

    public CommonGroup(String name, InternalAkaniCore core) {
        this.name = name;
        this.core = core;
    }


    @Override
    public void broadcast(Component message) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String name() {
        return name;
    }

    public void addServer(String server) {
        servers.add(server);
    }

    @Override
    public Set<String> serverNames() {
        return ImmutableSet.copyOf(servers);
    }

    @Override
    public void runCommand(String command) {
        for (String server : servers) {
            core.commandHandler().runCommandAsServer(server, command);
        }
    }
}
