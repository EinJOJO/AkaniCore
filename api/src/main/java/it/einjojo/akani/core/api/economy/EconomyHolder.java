package it.einjojo.akani.core.api.economy;

public interface EconomyHolder {

    long balance();

    void setBalance(long balance) throws BadBalanceException;

    void addBalance(long balance) throws BadBalanceException;

    void removeBalance(long balance) throws BadBalanceException;

}
