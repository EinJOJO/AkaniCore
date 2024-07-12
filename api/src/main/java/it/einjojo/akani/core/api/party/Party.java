package it.einjojo.akani.core.api.party;

import it.einjojo.akani.core.api.player.AkaniPlayer;

import java.util.Set;
import java.util.UUID;

public interface Party {
    /**
     * Get the party id.
     */
    int partyId();

    /**
     * Get the owner of the party.
     */
    UUID ownerUuid();

    /**
     * Get the owner of the party.
     */
    AkaniPlayer owner();

    /**
     * Check if the player is the owner of the party.
     *
     * @param uuid The player to check
     * @return true if the player is the owner of the party
     */
    boolean isOwner(UUID uuid);

    /**
     * Get the members of the party.
     */
    Set<UUID> memberUuids();

    /**
     * Get the invited players of the party.
     */
    Set<UUID> invitedUuids();

    /**
     * Get the members of the party.
     */
    Set<AkaniPlayer> members();

    /**
     * Add a member to the party.
     * When a member is added:
     * 1. party should notify all members of the party that a new member has joined.
     * 2. The party should also notify the new member of all existing members.
     * 3. The player should be connected to the party server
     *
     * @param player The player to add
     */
    void addMember(AkaniPlayer player);

    /**
     * Add a member to the party without sending any notifications.
     * This method should be used when the party is being created or when a player is rejoining the party.
     *
     * @param player The player to add
     */
    void addMemberSilent(UUID player);

    /**
     * Remove a member from the party.
     * When a member is removed:
     * 1. party should notify all members of the party that a member has left.
     * 2. The player should be disconnected from the party server
     *
     * @param player The player to remove
     */
    void removeMember(AkaniPlayer player);

    /**
     * Remove a member from the party without sending any notifications.
     * This method should be used when the party is being deleted or when a player is leaving the party.
     *
     * @param player The player to remove
     */
    void removeMemberSilent(UUID player);

    /**
     * Disbands the party.
     */
    void disband();

    /**
     * Kick a player from the party.
     * When a player is kicked:
     * 1. party should notify all members of the party that a member has been kicked.
     * 2. The player should be disconnected from the party server
     *
     * @param player The player to kick
     */
    void kick(AkaniPlayer player);

    /**
     * Invite a player to the party.
     * When a player is invited:
     * 1. The player will be notified of the invitation.
     *
     * @param player The player to invite
     */
    void invite(AkaniPlayer player);

    /**
     * When a player gets promoted by the owner;
     *
     * @param player The player to accept
     */
    void setOwner(AkaniPlayer player);
}
