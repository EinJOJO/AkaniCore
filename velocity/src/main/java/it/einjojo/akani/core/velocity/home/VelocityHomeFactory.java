package it.einjojo.akani.core.velocity.home;

import it.einjojo.akani.core.api.AkaniCore;
import it.einjojo.akani.core.api.home.Home;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.home.HomeFactory;

import java.util.UUID;

public class VelocityHomeFactory implements HomeFactory {
    private final AkaniCore core;

    public VelocityHomeFactory(AkaniCore core) {
        this.core = core;
    }

    @Override
    public Home createHome(UUID owner, String name, NetworkLocation location) {
        return new VelocityHome(owner, name, location, core);
    }
}
