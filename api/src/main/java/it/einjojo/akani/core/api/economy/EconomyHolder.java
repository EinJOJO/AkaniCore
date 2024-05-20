package it.einjojo.akani.core.api.economy;

import java.util.UUID;

public interface EconomyHolder {
    UUID ownerUuid();

    long balance();

    /**
     * @param balance the new balance
     * @throws BadBalanceException
     */
    void setBalance(long balance);

    /**
     * @param balance the amount to add
     * @throws BadBalanceException
     */
    void addBalance(long balance);

    /**
     * @param balance the amount to remove
     * @throws BadBalanceException
     */
    void removeBalance(long balance);

}
