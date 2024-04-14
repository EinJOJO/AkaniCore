package it.einjojo.akani.core.api.economy;

public interface PlayerEconomy {

    double balance();

    double setBalance(double balance) throws BadBalanceException;

    double addBalance(double balance) throws BadBalanceException;

    double removeBalance(double balance) throws BadBalanceException;



}
