package it.einjojo.akani.core.economy;

import it.einjojo.akani.core.api.economy.BadBalanceException;
import it.einjojo.akani.core.api.economy.EconomyHolder;

import java.util.concurrent.atomic.AtomicLong;

public class ImplEconomyHolder implements EconomyHolder {
    private final AtomicLong balance;
    private boolean hasChanged = false;

    public ImplEconomyHolder(long balance) {
        this.balance = new AtomicLong(balance);
    }

    public boolean hasChanged() {
        return hasChanged;
    }

    @Override
    public long balance() {
        return balance.get();
    }

    @Override
    public void setBalance(long balance) throws BadBalanceException {
        change(balance);
    }

    @Override
    public void addBalance(long balance) throws BadBalanceException {
        change(balance() + balance);

    }

    @Override
    public void removeBalance(long balance) throws BadBalanceException {
        long newBalance = balance() - balance;
        if (newBalance < 0) {
            throw BadBalanceException.notEnoughFunds();
        }
        change(newBalance);
    }

    private void change(long newBalance) throws BadBalanceException {
        if (newBalance < 0) {
            throw BadBalanceException.negativeBalance();
        }
        hasChanged = true;
        balance.set(newBalance);
    }
}
