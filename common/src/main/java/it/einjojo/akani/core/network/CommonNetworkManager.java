package it.einjojo.akani.core.network;

import it.einjojo.akani.core.InternalAkaniCore;
import it.einjojo.akani.core.api.network.Group;
import it.einjojo.akani.core.api.network.NetworkManager;
import it.einjojo.akani.core.api.network.Server;

import java.util.List;
import java.util.Optional;

//TODO
public record CommonNetworkManager(InternalAkaniCore core) implements NetworkManager {
    @Override
    public Optional<Server> server(String name) {
        return Optional.empty();
    }

    @Override
    public List<Server> servers() {
        return List.of();
    }

    @Override
    public Optional<Group> group(String name) {
        return Optional.empty();
    }

    @Override
    public List<String> groups() {
        return List.of();
    }

    public void register(Server server) {

    }

    @Override
    public void unregister(Server server) {

    }
}
