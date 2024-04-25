package it.einjojo.akani.core.api.party;

import it.einjojo.akani.core.api.player.AkaniPlayer;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface PartyManager {

    Collection<Party> parties();

    Optional<Party> party(int partyId);

    boolean isInsideParty(UUID uuid);

    boolean canBeInvited(AkaniPlayer player);

    boolean canCreateParty(AkaniPlayer player);

    void createParty(AkaniPlayer partyOwner);

    void invite(AkaniPlayer player, Party party);

}
