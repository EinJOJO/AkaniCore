package it.einjojo.akani.core.home;

import it.einjojo.akani.core.api.home.Home;
import it.einjojo.akani.core.api.home.HomeHolder;
import org.jetbrains.annotations.Blocking;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record CommonHomeHolder(UUID owner, List<Home> homes, CommonHomeStorage storage) implements HomeHolder {
    public Optional<Home> home(String name) {
        for (Home home : homes) {
            if (home.name().equalsIgnoreCase(name)) {
                return Optional.of(home);
            }
        }
        return Optional.empty();
    }

    public int homeCount() {
        return homes.size();
    }

    public boolean hasHome(String name) {
        return home(name).isPresent();
    }


    /**
     * Add a home to the player's home list
     *
     * @param home the home to add
     * @return true if the home was added, false if the home could not be added
     * @throws IllegalArgumentException if the home-owner is not the same as the holder
     */
    @Blocking
    public boolean addHome(Home home) {
        if (!home.owner().equals(owner)) {
            throw new IllegalArgumentException("Home owner must be the same as the holder");
        }
        if (hasHome(home.name())) {
            return false;
        }
        if (!storage.saveHome(home)) return false;
        homes.add(home);
        return true;
    }

    @Blocking
    public boolean removeHome(String name) {
        Home home = home(name).orElse(null);
        if (home == null) {
            return false;
        }
        if (!storage.deleteHome(home.owner(), home.name())) return false;
        return homes.remove(home);
    }


}
