package it.einjojo.akani.core.api.economy;

import java.util.UUID;

public interface EconomyHolder {
    UUID ownerUuid();

    long balance();

    void setBalance(long balance) throws BadBalanceException;

    void addBalance(long balance) throws BadBalanceException;

    void removeBalance(long balance) throws BadBalanceException;

}
