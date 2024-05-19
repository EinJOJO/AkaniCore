package it.einjojo.akani.core.api.home;

import java.util.List;
import java.util.Optional;

public interface HomeHolder {

    List<Home> homes();

    boolean addHome(Home home);

    boolean removeHome(String homeName);

    boolean hasHome(String homeName);

    Optional<Home> home(String homeName);

    int homeCount();

}
