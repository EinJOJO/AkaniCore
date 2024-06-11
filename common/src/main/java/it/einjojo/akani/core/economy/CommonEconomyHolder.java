package it.einjojo.akani.core.economy;

import it.einjojo.akani.core.api.economy.BadBalanceException;
import it.einjojo.akani.core.api.economy.EconomyHolder;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CommonEconomyHolder implements EconomyHolder {
    private final UUID uuid;
    private final AtomicLong atomicBalance;
    private EconomyObserver observer;
    private boolean hasChanged = false;

    public CommonEconomyHolder(UUID uuid, long balance) {
        this.uuid = uuid;
        this.atomicBalance = new AtomicLong(balance);
    }

    public void setObserver(EconomyObserver observer) {
        this.observer = observer;
    }

    @Override
    public UUID ownerUuid() {
        return uuid;
    }

    public boolean hasChanged() {
        return hasChanged;
    }

    @Override
    public long balance() {
        return atomicBalance.get();
    }

    public void setBalanceWithoutNotify(long balance) {
        atomicBalance.set(balance);
    }

    @Override
    public void setBalance(long balance) throws BadBalanceException {
        if (balance < 0) {
            throw BadBalanceException.negativeBalance();
        }
        change(balance);
    }

    @Override
    public void addBalance(long balance) throws BadBalanceException {
        if (balance < 0) {
            throw BadBalanceException.negativeBalance();
        }
        change(balance() + balance);
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
        change(newBalance);
    }

    private void change(long newBalance) throws BadBalanceException {
        if (newBalance < 0) {
            throw BadBalanceException.negativeBalance();
        }
        if (newBalance == balance()) {
            return;
        }
        if (observer != null) {
            observer.onBalanceChange(this, balance(), newBalance);
        }
        hasChanged = true;
        atomicBalance.set(newBalance);
    }
}
