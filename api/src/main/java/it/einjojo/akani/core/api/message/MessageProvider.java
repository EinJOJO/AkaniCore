package it.einjojo.akani.core.api.message;

/**
 * For external plugins extending the message system
 */
public interface MessageProvider {
    /**
     * The name of the provider.
     * Can be for example "Essentials" or "LuckPerms"
     *
     * @return the name of the provider
     */
    String providerName();

    /**
     * In order to reduce the amount of requests to the database, this method is used to check if insertMessages() should be run.
     *
     * @param storage the storage to check
     * @return if the storage should be registered to the database.
     */
    boolean shouldInsert(MessageStorage storage);

    /**
     * Inserts the messages into the database
     * @param storage the storage to insert
     */
    void insertMessages(MessageStorage storage);


}
