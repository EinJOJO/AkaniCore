package it.einjojo.akani.core.home;

import it.einjojo.akani.core.api.home.Home;
import it.einjojo.akani.core.api.network.NetworkLocation;

import java.util.UUID;

public interface HomeFactory {
    Home createHome(UUID owner, String name, NetworkLocation location);
}
