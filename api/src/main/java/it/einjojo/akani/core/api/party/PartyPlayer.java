package it.einjojo.akani.core.api.party;

import java.util.Optional;
import java.util.UUID;

public interface PartyPlayer {
    UUID uuid();
    String name();

    Optional<Party> party();
}
