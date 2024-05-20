package it.einjojo.akani.core.api.home;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface HomeHolder {

    List<Home> homes();

    boolean addHome(@NotNull Home home);

    boolean removeHome(@Nullable String homeName);

    boolean hasHome(@Nullable String homeName);

    Optional<Home> home(@Nullable String homeName);

    int homeCount();

}
