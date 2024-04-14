package it.einjojo.akani.core.economy;

import it.einjojo.akani.core.api.economy.BadBalanceException;
import it.einjojo.akani.core.api.economy.EconomyHolder;

import java.util.concurrent.atomic.AtomicLong;

public class ImplEconomyHolder implements EconomyHolder {
    private final AtomicLong balance;

    public ImplEconomyHolder(long balance) {
        this.balance = new AtomicLong(balance);
    }

    @Override
    public long balance() {
        return balance.get();
    }

    @Override
    public void setBalance(long balance) throws BadBalanceException {
        if (balance < 0) {
            throw BadBalanceException.negativeBalance();
        }
        this.balance.getAndSet(balance);
    }

    @Override
    public void addBalance(long balance) throws BadBalanceException {
        if (balance < 0) {
            throw BadBalanceException.negativeBalance();
        }
        this.balance.set(balance() + balance);
    }

    @Override
    public void removeBalance(long balance) throws BadBalanceException {
        if (balance < 0) {
            throw BadBalanceException.negativeBalance();
        }
        long newBalance = balance() - balance;
        if (newBalance < 0) {
            throw BadBalanceException.notEnoughFunds();
        }
        this.balance.set(newBalance);
    }
}
